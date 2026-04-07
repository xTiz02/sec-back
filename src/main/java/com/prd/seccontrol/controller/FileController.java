package com.prd.seccontrol.controller;

import com.prd.seccontrol.extra.ExcelValidationException;
import com.prd.seccontrol.extra.GenericException;
import com.prd.seccontrol.model.configdto.ClientUnit;
import com.prd.seccontrol.model.configdto.GuardSchedule;
import com.prd.seccontrol.model.configdto.MonthlySchedule;
import com.prd.seccontrol.service.impl.DateGuardUnityAssignmentService;
import com.prd.seccontrol.service.inter.ScheduleExcelReaderService;
import com.prd.seccontrol.util.SEConstants;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

  @Autowired
  private ScheduleExcelReaderService readerService;

  @Autowired
  private DateGuardUnityAssignmentService assignmentService;


  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/storage/schedule/upload-file/validate")
  public ResponseEntity<?> uploadValidateSchedule(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      throw new GenericException("El archivo está vacío.");
    }
    // todo validar que existe el contrato,, que las unidades existan en el contrato y que los guardias existan
    try {
      MonthlySchedule schedule = readerService.readFromMultipart(file, true);

//      printFullDetail(schedule);

      return ResponseEntity.ok(Map.of(
          "yearMonth",  schedule.getYearMonth().toString(),
          "totalDays",  schedule.getTotalDays(),
          "cliente",    schedule.getCliCode() + " - " + schedule.getCliName(),
          "unidades",   schedule.getUnits().size(),
          "filasExcel", schedule.getRawRows().size(),
          "contractCode", schedule.getContractCode()
      ));

    } catch (ExcelValidationException ex) {
      // Error de formato conocido: devolvemos posición y descripción
      return ResponseEntity.ok(Map.of(
          "position",    ex.getPosition(),
          "description", ex.getDescription()
      ));

    } catch (IOException ex) {
      throw new GenericException("No se pudo leer el archivo: " + ex.getMessage());
    }
  }

  @PostMapping(SEConstants.SECURE_BASE_ENDPOINT + "/storage/schedule/upload-file/create")
  public ResponseEntity<?> uploadCreateSchedule(@RequestParam("file") MultipartFile file)
      throws Exception {
    return ResponseEntity.ok(assignmentService.createsDatesGuardAssignmentsFromExcel(file));
  }

  private void printFullDetail(MonthlySchedule schedule) {

    System.out.printf("%n════════════════════════════════════════%n");
    System.out.printf("  %s %s  |  %s - %s%n",
        schedule.getYearMonth(), "",
        schedule.getCliCode(), schedule.getCliName());
    System.out.printf("════════════════════════════════════════%n");

    for (ClientUnit unit : schedule.getUnits()) {
      System.out.printf("%n  ▶ UNIDAD [%s] %s%n", unit.getUCode(), unit.getUName());

      for (GuardSchedule guard : unit.getGuards()) {

        System.out.printf("%n    Guardia: [%s] %s  (%d fila(s) fuente)%n",
            guard.getViCode(), guard.getViName(),
            guard.getSourceRows().size());

        // ── Opción A: recorrer el mapa consolidado (ya fusionado) ──
        guard.getMergedShifts().forEach((day, cell) -> {
          System.out.printf("      Día %2d → %-2s   [Excel: fila=%d, col=%d (%s), ref=%s]%n",
              day, cell.getShift(),
              cell.getExcelRow(), cell.getExcelCol(),
              cell.getExcelColLetter(), cell.getCellRef());
        });

        // ── Opción B: por día específico ──────────────────────────
        // String turno = guard.getShiftForDay(15);       // "D","N","X","VC" o null
        // ShiftCell cell = guard.getShiftCellForDay(15); // con posición Excel

        // ── Opción C: ver qué fila Excel aportó cada turno ────────
        // for (ScheduleRow raw : guard.getSourceRows()) {
        //     System.out.println("  Fila Excel #" + raw.getExcelRowNumber()
        //             + " aportó " + raw.getShifts().size() + " turnos");
        // }
      }
    }

    // ── Filas crudas (auditoría) ──────────────────────────────
    System.out.printf("%n  Total filas leídas del Excel: %d%n", schedule.getRawRows().size());
  }
}
