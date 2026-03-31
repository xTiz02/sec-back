package com.prd.seccontrol.service.inter;

import com.prd.seccontrol.extra.ExcelValidationException;
import com.prd.seccontrol.model.configdto.ClientUnit;
import com.prd.seccontrol.model.configdto.GuardSchedule;
import com.prd.seccontrol.model.configdto.MonthlySchedule;
import com.prd.seccontrol.model.configdto.ScheduleRow;
import com.prd.seccontrol.model.configdto.ShiftCell;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ScheduleExcelReaderService {

  private static final int COL_VI_CODE = 0; // A
  private static final int COL_VI_NAME = 1; // B
  private static final int COL_CLI_CODE = 2; // C
  private static final int COL_CLI_NAME = 3; // D
  private static final int COL_U_CODE = 4; // E
  private static final int COL_U_NAME = 5; // F
  private static final int COL_DAYS_START = 6; // G → día 1

  private static final int ROW_MONTH = 0;
  private static final int ROW_HEADERS = 1;
  private static final int ROW_DATA_START = 2;

  private static final Set<String> VALID_SHIFTS = Set.of("D", "N", "X", "VC");
  private static final Map<String, Integer> MONTH_MAP = buildMonthMap();

  // ── Entrada pública ───────────────────────────────────────────

  public MonthlySchedule readFromMultipart(MultipartFile file) throws IOException {
    try (InputStream is = file.getInputStream()) {
      return readFromInputStream(is);
    }
  }

  public MonthlySchedule readFromInputStream(InputStream inputStream) throws IOException {
    try (Workbook workbook = new XSSFWorkbook(inputStream)) {

      Sheet sheet = workbook.getSheetAt(0);

      // V1: mes y año
      YearMonth yearMonth = parseYearMonth(sheet);
      int totalDays = yearMonth.lengthOfMonth();
      int lastDayCol = resolveLastDayColumn(sheet, totalDays);
      String contractCode = getCellStr(sheet.getRow(ROW_MONTH).getCell(2)).trim();

      // seenShifts: "viCode|uCode" → { día → referencia de celda donde ya se definió }
      // Permite detectar V4 a medida que se van leyendo las filas.
      Map<String, Map<Integer, String>> seenShifts = new HashMap<>();
      String expectedCliCode = null;

      List<ScheduleRow> rawRows = new ArrayList<>();

      for (int rowIdx = ROW_DATA_START; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
        Row row = sheet.getRow(rowIdx);
        if (row == null || isRowEmpty(row)) {
          continue;
        }

        int excelRow = rowIdx + 1;

        // V2: todos los identificadores deben estar presentes
        String viCode = requireNonEmpty(row, COL_VI_CODE, excelRow, "VI_CODE (código de guardia)");
        String viName = requireNonEmpty(row, COL_VI_NAME, excelRow, "VI_NAME (nombre de guardia)");
        String cliCode = requireNonEmpty(row, COL_CLI_CODE, excelRow,
            "CLI_CODE (código de cliente)");
        String cliName = requireNonEmpty(row, COL_CLI_NAME, excelRow,
            "CLI_NAME (nombre de cliente)");
        String uCode = requireNonEmpty(row, COL_U_CODE, excelRow, "U_CODE (código de unidad)");
        String uName = requireNonEmpty(row, COL_U_NAME, excelRow, "U_NAME (nombre de unidad)");

        // V3: CLI_CODE único por archivo
        if (expectedCliCode == null) {
          expectedCliCode = cliCode;
        } else if (!expectedCliCode.equals(cliCode)) {
          throw new ExcelValidationException(
              cellRef("C", excelRow),
              "Se encontró el cliente '" + cliCode + "' pero se esperaba '"
                  + expectedCliCode + "'. Un archivo solo puede contener un cliente.");
        }

        // V4: turno duplicado para mismo guardia+unidad+día
        String guardKey = viCode + "|" + uCode;
        Map<Integer, String> guardDays = seenShifts.computeIfAbsent(guardKey, k -> new HashMap<>());

        List<ShiftCell> shifts = parseAndValidateShifts(row, lastDayCol, excelRow, guardDays,
            viCode, uCode);

        // Registrar los nuevos días después de validar (sin conflicto)
        for (ShiftCell sc : shifts) {
          guardDays.put(sc.getDay(), sc.getCellRef());
        }

        ScheduleRow scheduleRow = new ScheduleRow(viCode, viName, uCode, uName, excelRow, shifts);
        scheduleRow.setExcelCliCode(cliCode);
        scheduleRow.setExcelCliName(cliName);
        rawRows.add(scheduleRow);
      }

      if (rawRows.isEmpty()) {
        throw new ExcelValidationException("Fila 3",
            "El archivo no contiene filas de datos a partir de la fila 3.");
      }

      String finalCliCode = rawRows.get(0).getExcelCliCode();
      String finalCliName = rawRows.get(0).getExcelCliName();
      List<ClientUnit> units = buildUnits(rawRows);

      return new MonthlySchedule(contractCode,yearMonth, totalDays, finalCliCode, finalCliName, rawRows, units);
    }
  }

  // ── V2: campo obligatorio ─────────────────────────────────────

  private String requireNonEmpty(Row row, int col, int excelRow, String fieldName) {
    String value = getCellStr(row.getCell(col)).trim();
    if (value.isEmpty()) {
      throw new ExcelValidationException(
          cellRef(columnIndexToLetter(col), excelRow),
          "El campo " + fieldName + " no puede estar vacío.");
    }
    return value;
  }

  // ── V4: turno duplicado ───────────────────────────────────────

  private List<ShiftCell> parseAndValidateShifts(Row row, int lastDayCol, int excelRow,
      Map<Integer, String> guardDays,
      String viCode, String uCode) {
    List<ShiftCell> cells = new ArrayList<>();

    for (int colIdx = COL_DAYS_START; colIdx <= lastDayCol; colIdx++) {
      String value = getCellStr(row.getCell(colIdx)).toUpperCase().trim();
      if (value.isEmpty() || !VALID_SHIFTS.contains(value)) {
        continue;
      }

      int day = colIdx - COL_DAYS_START + 1;
      String colLet = columnIndexToLetter(colIdx);
      String ref = cellRef(colLet, excelRow);

      if (guardDays.containsKey(day)) {
        String prevRef = guardDays.get(day);
        throw new ExcelValidationException(ref,
            "El guardia " + viCode + " en la unidad " + uCode
                + " ya tiene turno definido para el día " + day
                + " (fijado anteriormente en " + prevRef + ")."
                + " Si esta fila completa días faltantes, la celda debe estar vacía.");
      }

      cells.add(new ShiftCell(day, value, excelRow, colIdx + 1, colLet));
    }

    return cells;
  }

  // ── Jerarquía ─────────────────────────────────────────────────

  private List<ClientUnit> buildUnits(List<ScheduleRow> rawRows) {
    Map<String, Map<String, List<ScheduleRow>>> byUnitByGuard = new LinkedHashMap<>();

    for (ScheduleRow row : rawRows) {
      String uCode = row.getUCode() != null ? row.getUCode() : "";
      String viCode = row.getViCode() != null ? row.getViCode() : "";
      if (viCode.isEmpty()) {
        continue;
      }
      byUnitByGuard
          .computeIfAbsent(uCode, k -> new LinkedHashMap<>())
          .computeIfAbsent(viCode, k -> new ArrayList<>())
          .add(row);
    }

    List<ClientUnit> units = new ArrayList<>();
    for (Map.Entry<String, Map<String, List<ScheduleRow>>> unitEntry : byUnitByGuard.entrySet()) {
      String uCode = unitEntry.getKey();
      String uName = unitEntry.getValue().values().stream()
          .flatMap(List::stream).map(ScheduleRow::getUName)
          .filter(n -> n != null && !n.isEmpty()).findFirst().orElse("");

      List<GuardSchedule> guards = new ArrayList<>();
      for (Map.Entry<String, List<ScheduleRow>> ge : unitEntry.getValue().entrySet()) {
        List<ScheduleRow> rows = ge.getValue();
        guards.add(new GuardSchedule(ge.getKey(), rows.get(0).getViName(), uCode, uName, rows));
      }
      units.add(new ClientUnit(uCode, uName, guards));
    }
    return units;
  }

  // ── Cabeceras ─────────────────────────────────────────────────

  private YearMonth parseYearMonth(Sheet sheet) {
    Row row = sheet.getRow(ROW_MONTH);
    if (row == null) {
      throw new ExcelValidationException("Fila 1",
          "La fila 1 no existe. Se esperaba nombre del mes en A1 y año en B1.");
    }

    String monthName = getCellStr(row.getCell(0)).toUpperCase().trim();
    String yearStr = getCellStr(row.getCell(1)).trim();
    String contractCode = getCellStr(row.getCell(2)).trim();

    if (monthName.isEmpty()) {
      throw new ExcelValidationException("A1", "El nombre del mes no puede estar vacío.");
    }

    Integer month = MONTH_MAP.get(monthName);
    if (month == null) {
      throw new ExcelValidationException("A1",
          "Mes no reconocido: '" + monthName
              + "'. Use el nombre en español (ENERO, FEBRERO, ...).");
    }

    if (yearStr.isEmpty()) {
      throw new ExcelValidationException("B1", "El año no puede estar vacío en B1.");
    }

    if(contractCode.isEmpty()) {
      throw new ExcelValidationException("C1", "El código de contrato no puede estar vacío en C1.");
    }

    int year;
    try {
      year = (int) Double.parseDouble(yearStr);
    } catch (NumberFormatException e) {
      throw new ExcelValidationException("B1", "El año '" + yearStr + "' no es un número válido.");
    }

    return YearMonth.of(year, month);
  }

  private int resolveLastDayColumn(Sheet sheet, int totalDays) {
    Row headerRow = sheet.getRow(ROW_HEADERS);
    if (headerRow == null) {
      return COL_DAYS_START + totalDays - 1;
    }
    int lastCol = COL_DAYS_START;
    for (int col = COL_DAYS_START; col < COL_DAYS_START + totalDays; col++) {
      if (getCellStr(headerRow.getCell(col)).isBlank()) {
        break;
      }
      lastCol = col;
    }
    return lastCol;
  }

  // ── Helpers ───────────────────────────────────────────────────

  private String getCellStr(Cell cell) {
    if (cell == null) {
      return "";
    }
    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue();
      case NUMERIC -> {
        double d = cell.getNumericCellValue();
        yield (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d);
      }
      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
      case FORMULA -> {
        try {
          yield cell.getStringCellValue();
        } catch (Exception e) {
          yield String.valueOf((long) cell.getNumericCellValue());
        }
      }
      default -> "";
    };
  }

  private boolean isRowEmpty(Row row) {
    for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
      Cell cell = row.getCell(c);
      if (cell != null && cell.getCellType() != CellType.BLANK
          && !getCellStr(cell).isBlank()) {
        return false;
      }
    }
    return true;
  }

  private String cellRef(String colLetter, int excelRow) {
    return colLetter + excelRow;
  }

  private String columnIndexToLetter(int colIdx) {
    StringBuilder sb = new StringBuilder();
    int n = colIdx + 1;
    while (n > 0) {
      n--;
      sb.insert(0, (char) ('A' + (n % 26)));
      n /= 26;
    }
    return sb.toString();
  }

  private static Map<String, Integer> buildMonthMap() {
    Map<String, Integer> m = new HashMap<>();
    m.put("ENERO", 1);
    m.put("FEBRERO", 2);
    m.put("MARZO", 3);
    m.put("ABRIL", 4);
    m.put("MAYO", 5);
    m.put("JUNIO", 6);
    m.put("JULIO", 7);
    m.put("AGOSTO", 8);
    m.put("SEPTIEMBRE", 9);
    m.put("SETIEMBRE", 9);
    m.put("OCTUBRE", 10);
    m.put("NOVIEMBRE", 11);
    m.put("DICIEMBRE", 12);
    return Collections.unmodifiableMap(m);
  }
}
