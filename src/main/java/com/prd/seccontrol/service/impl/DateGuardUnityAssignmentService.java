package com.prd.seccontrol.service.impl;

import com.prd.seccontrol.model.configdto.ClientUnit;
import com.prd.seccontrol.model.configdto.GuardSchedule;
import com.prd.seccontrol.model.configdto.MonthlySchedule;
import com.prd.seccontrol.model.configdto.ShiftCell;
import com.prd.seccontrol.model.configdto.VacationRange;
import com.prd.seccontrol.model.dto.ContractScheduleSummaryDto;
import com.prd.seccontrol.model.dto.CreateBulkFreeDayRequest;
import com.prd.seccontrol.model.dto.CreateBulkVacationRequest;
import com.prd.seccontrol.model.dto.CreateDailyAssignmentRequest;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentDto;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo;
import com.prd.seccontrol.model.dto.GuardUnityScheduleSummaryDto;
import com.prd.seccontrol.model.entity.ClientContract;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.entity.DayOfMonth;
import com.prd.seccontrol.model.entity.GuardAssignment;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import com.prd.seccontrol.model.entity.ScheduleMonthly;
import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.entity.WeekOfMonth;
import com.prd.seccontrol.model.types.DayOfWeek;
import com.prd.seccontrol.model.types.GuardType;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import com.prd.seccontrol.model.types.TurnType;
import com.prd.seccontrol.repository.ClientContractRepository;
import com.prd.seccontrol.repository.ClientRepository;
import com.prd.seccontrol.repository.ContractScheduleUnitTemplateRepository;
import com.prd.seccontrol.repository.DateGuardUnityAssignmentRepository;
import com.prd.seccontrol.repository.DayOfMonthRepository;
import com.prd.seccontrol.repository.GuardAssignmentRepository;
import com.prd.seccontrol.repository.GuardRepository;
import com.prd.seccontrol.repository.GuardUnityScheduleAssignmentRepository;
import com.prd.seccontrol.repository.ScheduleMonthlyRepository;
import com.prd.seccontrol.repository.TurnAndHourRepository;
import com.prd.seccontrol.repository.TurnTemplateRepository;
import com.prd.seccontrol.repository.WeekOfMonthRepository;
import com.prd.seccontrol.service.inter.ScheduleExcelReaderService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DateGuardUnityAssignmentService {

  @Autowired
  private DateGuardUnityAssignmentRepository dateGuardUnityAssignmentRepository;

  @Autowired
  private ScheduleMonthlyRepository scheduleMonthlyRepository;

  @Autowired
  private DayOfMonthRepository dayOfMonthRepository;

  @Autowired
  private WeekOfMonthRepository weekOfMonthRepository;

  @Autowired
  private GuardUnityScheduleAssignmentRepository guardUnityScheduleAssignmentRepository;

  @Autowired
  private GuardAssignmentRepository guardAssignmentRepository;

  @Autowired
  private TurnAndHourRepository turnAndHourRepository;

  @Autowired
  private TurnTemplateRepository turnTemplateRepository;

  @Autowired
  private TurnTemplateService turnTemplateService;

  @Autowired
  private ScheduleExcelReaderService scheduleExcelReaderService;

  @Autowired
  private ClientContractRepository clientContractRepository;

  @Autowired
  private GuardRepository guardRepository;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private ContractScheduleUnitTemplateRepository contractScheduleUnitTemplateRepository;

  @Transactional
  public DateGuardUnityAssignmentDto createDateGuardUnityAssignment(
      CreateDailyAssignmentRequest request, LocalDate dateTo) {
    LocalDate date = request.date();
    Integer year = date.getYear();
    Month monthEnum = date.getMonth();
    Integer month = monthEnum.getValue();
    Integer day = date.getDayOfMonth();

    //todo validar que el guardia no tenga otra asistencia en otra guardia para el mismo día y hora
    List<DateGuardUnityAssignmentSimpleInfo> existingAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentIdAndDate(request.guardUnityScheduleAssignmentId(),
            date);
    if (!existingAssignments.isEmpty()) {
      throw new RuntimeException("El guardia ya tiene una asignación para el día " + date);
    }

    TurnTemplate turnTemplate =
        request.scheduleAssignmentType().equals(ScheduleAssignmentType.VACATIONAL) ? null
            : turnTemplateRepository.findTurnTemplateByTurnAndHourId(
                    request.turnAndHourId())
                .orElseThrow(() -> new RuntimeException(
                    "TurnTemplate not found for TurnAndHourId: " + request.turnAndHourId()));
    DateGuardUnityAssignment dgua = new DateGuardUnityAssignment();
    dgua.setGuardUnityScheduleAssignmentId(request.guardUnityScheduleAssignmentId());
    dgua.setTurnAndHourId(request.turnAndHourId());
    dgua.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(date.getDayOfWeek()));
    dgua.setNumDay(day);
    dgua.setDate(date);
    dgua.setToDate(dateTo);
    dgua.setScheduleAssignmentType(request.scheduleAssignmentType());
    dgua.setHasVacation(false);
    dgua.setHasExceptions(false);
    dgua.setFinalized(false);

    if (turnTemplate != null) {
      LocalDateTime[] dateTimes = turnTemplateService.getShiftDateTimeRange(date, turnTemplate);
      dgua.setDateTimeEntry(dateTimes[0]);
      dgua.setDateTimeEnd(dateTimes[1]);
    }

    processDayOfMonth(monthEnum, year, day, date, dgua);

    DateGuardUnityAssignment saved = dateGuardUnityAssignmentRepository.save(dgua);
    return new DateGuardUnityAssignmentDto(saved);
  }

  @Transactional
  public List<DateGuardUnityAssignmentDto> bulkCreateFreeDayAssignments(
      CreateBulkFreeDayRequest request) {
    List<DateGuardUnityAssignmentSimpleInfo> existingFreeDaysAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentIdAndDateIn(
            request.guardUnityScheduleAssignmentId(), request.dates());

    List<DateGuardUnityAssignmentDto> createdAssignments = request.dates().stream()
        .filter(date -> existingFreeDaysAssignments.stream()
            .noneMatch(existing -> existing.date().equals(date)))
        .map(date -> {
          CreateDailyAssignmentRequest dailyRequest = new CreateDailyAssignmentRequest(
              date,
              request.guardUnityScheduleAssignmentId(),
              null, // turnAndHourId no es necesario para días libres
              ScheduleAssignmentType.FREE_DAY
          );

          return createDateGuardUnityAssignment(dailyRequest, null);
        })
        .toList();

    return createdAssignments;
  }

  @Transactional
  public List<DateGuardUnityAssignmentDto> bulkCreateVacationAssignments(
      CreateBulkVacationRequest request) {
    List<DateGuardUnityAssignmentSimpleInfo> existingAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentIdAndDateIsBetween(
            request.guardUnityScheduleAssignmentId(), request.date(), request.toDate());

    if (!existingAssignments.isEmpty()) {
      // If there are existing assignments in the date range
      throw new RuntimeException(
          "El agente ya tiene asignaciones en el rango de fechas seleccionado para vacaciones.");
    }

    CreateDailyAssignmentRequest dailyRequest = new CreateDailyAssignmentRequest(
        request.date(),
        request.guardUnityScheduleAssignmentId(),
        null, // turnAndHourId no es necesario para vacaciones
        ScheduleAssignmentType.VACATIONAL);

    DateGuardUnityAssignmentDto createdAssignment = createDateGuardUnityAssignment(dailyRequest,
        request.toDate());

    return List.of(createdAssignment);
  }

  @Transactional
  public Long deleteDateGuardUnityAssignment(Long id) {
    DateGuardUnityAssignment assignment = dateGuardUnityAssignmentRepository.findById(id)
        .orElseThrow(
            () -> new RuntimeException("DateGuardUnityAssignment not found with id: " + id));

    List<DateGuardUnityAssignment> dateGuardUnityAssignments = dateGuardUnityAssignmentRepository
        .findByGuardUnityScheduleAssignmentId(
            assignment.getGuardUnityScheduleAssignmentId());

    dateGuardUnityAssignmentRepository.deleteById(id);
    if (dateGuardUnityAssignments.size() == 1) {
      guardUnityScheduleAssignmentRepository.deleteById(
          assignment.getGuardUnityScheduleAssignmentId());
      guardAssignmentRepository.deleteById(
          assignment.getGuardUnityScheduleAssignment().getGuardAssignmentId());
    }
    return id;
  }


  @Transactional
  public WeekOfMonth findOrCreateWeek(LocalDate date) {

    LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
    LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

    return weekOfMonthRepository
        .findByDateFromAndDateTo(startOfWeek, endOfWeek)
        .orElseGet(() -> {

          WeekOfMonth week = new WeekOfMonth();
          week.setInitDay(startOfWeek.getDayOfMonth());
          week.setInitMonth(startOfWeek.getMonthValue());
          week.setEndDay(endOfWeek.getDayOfMonth());
          week.setEndMonth(endOfWeek.getMonthValue());
          week.setYear(startOfWeek.getYear());
          week.setOtherYear(endOfWeek.getYear());
          week.setDateFrom(startOfWeek);
          week.setDateTo(endOfWeek);

          // opcional: índice de la semana dentro del mes
          week.setOrderIndex(startOfWeek.get(WeekFields.ISO.weekOfMonth()));

          return weekOfMonthRepository.save(week);
        });
  }

  @Transactional
  public Map<String,Long> createsDatesGuardAssignmentsFromExcel(MultipartFile file) throws Exception {
    //todo agregar una validadcion de toda carga se debe hacer maximo 1 dia antes de que empiece el mes.
    MonthlySchedule monthlySchedule = scheduleExcelReaderService.readFromMultipart(file);
    ClientContract clientContract = clientContractRepository.findByCode(
        monthlySchedule.getContractCode()).orElseThrow(() -> new RuntimeException(
        "ClientContract not found with code: " + monthlySchedule.getContractCode()));
    YearMonth yearMonth = monthlySchedule.getYearMonth();
    Month month = yearMonth.getMonth();
    int year = yearMonth.getYear();
    ScheduleMonthly scheduleMonthly = scheduleMonthlyRepository.findByMonthAndYear(
            month, year)
        .orElseGet(() -> {
          ScheduleMonthly newScheduleMonthly = new ScheduleMonthly();
          newScheduleMonthly.setName("Programación " + month + " " + year);
          newScheduleMonthly.setMonth(month);
          newScheduleMonthly.setYear(year);
          newScheduleMonthly.setDescription("Creado por Excel");
          return scheduleMonthlyRepository.save(newScheduleMonthly);
        });

    List<ContractScheduleSummaryDto> unitTemplates = contractScheduleUnitTemplateRepository.findByContractClientId(
        clientContract.getId());

    List<String> guardCodes = monthlySchedule.getUnits().stream()
        .flatMap(unit -> unit.getGuards().stream())
        .map(GuardSchedule::getViCode)
        .toList();

    List<Object[]> guardIdCode = guardRepository.findGuardIdsByCodeIn(guardCodes);

    List<Long> guardIds = guardIdCode.stream()
        .map(row -> (Long) row[0])
        .toList();

    List<GuardUnityScheduleSummaryDto> existenceGuardScheduleAssignments = guardUnityScheduleAssignmentRepository
        .findIdsByGuardIdAndScheduleMonthlyId(guardIds, scheduleMonthly.getId());

    //Create guardScheduleAssignment if not exist with guardCode
    List<GuardUnityScheduleSummaryDto> createdGuardScheduleAssignments = new ArrayList<>();
    for (Long guardId : guardIds) {
      boolean exists = existenceGuardScheduleAssignments.stream()
          .anyMatch(existing -> existing.guardId().equals(guardId));
      if (!exists) {
        GuardAssignment guardAssignment = new GuardAssignment();
        guardAssignment.setGuardId(guardId);
        guardAssignment.setActive(true);
        guardAssignment = guardAssignmentRepository.save(guardAssignment);

        GuardType guardType = guardIdCode.stream()
            .filter(row -> ((Long) row[0]).equals(guardId))
            .map(row -> (GuardType) row[2])
            .findFirst()
            .orElseThrow(
                () -> new RuntimeException("Guard type not found for guardId: " + guardId));
        GuardUnityScheduleAssignment newAssignment = new GuardUnityScheduleAssignment();
        newAssignment.setGuardAssignmentId(guardAssignment.getId());
        newAssignment.setScheduleMonthlyId(scheduleMonthly.getId());
        newAssignment.setGuardType(guardType);
        guardUnityScheduleAssignmentRepository.save(newAssignment);
        createdGuardScheduleAssignments.add(new GuardUnityScheduleSummaryDto(
            newAssignment.getId(),
            guardId,
            guardIdCode.stream()
                .filter(row -> ((Long) row[0]).equals(guardId))
                .map(row -> (String) row[1])
                .findFirst()
                .orElseThrow(
                    () -> new RuntimeException("Guard code not found for guardId: " + guardId)),
            guardType
        ));
      }
    }

    createdGuardScheduleAssignments.addAll(existenceGuardScheduleAssignments);

    List<DateGuardUnityAssignment> createdDateGuardUnityAssignments = new ArrayList<>();

    for (ClientUnit unit : monthlySchedule.getUnits()) {
      String unityCode = unit.getUCode();
      for (GuardSchedule guardSchedule : unit.getGuards()) {
        String viCode = guardSchedule.getViCode();

        GuardUnityScheduleSummaryDto gusa = createdGuardScheduleAssignments.stream()
            .filter(g -> g.guardCode().equals(viCode))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "No se encontró guardia con código " + viCode + " para asignación en unidad "
                    + unityCode));

        for (Entry<Integer, ShiftCell> entry : guardSchedule.getMergedShifts().entrySet()) {
          if (entry.getValue().getShift().equals("VC")) {
            continue;
          }
          int day = entry.getKey();
          LocalDate date = LocalDate.of(year, month, day);
          DayOfWeek dayOfWeek = DayOfWeek.fromJavaDayOfWeek(date.getDayOfWeek());

          DateGuardUnityAssignment dgua = new DateGuardUnityAssignment();
          if (isTurnShift(entry.getValue().getShift())) {
            ContractScheduleSummaryDto unitTemplate = unitTemplates.stream()
                .filter(template -> template.dayOfWeek().equals(dayOfWeek) &&
                    template.unityCode().equals(unityCode) && template.turnType()
                    .equals(getTurnTypeFromShift(entry.getValue().getShift())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                    "No se encontró plantilla para unidad " + unityCode + " día " + dayOfWeek +
                        " turno " + entry.getValue().getShift()));

            LocalDateTime[] dateTimes = turnTemplateService.getShiftDateTimeRange(date,
                new TurnTemplate(unitTemplate.startTime(), unitTemplate.endTime()));

            dgua.setDateTimeEntry(dateTimes[0]);
            dgua.setDateTimeEnd(dateTimes[1]);
            dgua.setDayOfWeek(dayOfWeek);
            dgua.setTurnAndHourId(unitTemplate.turnAndHourId());
            dgua.setScheduleAssignmentType(ScheduleAssignmentType.NORMAL);
            dgua.setGuardUnityScheduleAssignmentId(gusa.gusaId());
            dgua.setNumDay(day);
            dgua.setDate(date);
            dgua.setHasVacation(false);
            dgua.setHasExceptions(false);
            dgua.setFinalized(false);

          } else if (isScheduleAssignmentShift(entry.getValue().getShift())) {
            dgua.setScheduleAssignmentType(
                getScheduleAssignmentTypeFromShift(entry.getValue().getShift()));
            dgua.setGuardUnityScheduleAssignmentId(gusa.gusaId());
            dgua.setNumDay(day);
            dgua.setDayOfWeek(dayOfWeek);
            dgua.setDate(date);
            dgua.setHasVacation(false);
            dgua.setHasExceptions(false);
            dgua.setFinalized(false);
          }

          processDayOfMonth(month, year, day, date, dgua);

          createdDateGuardUnityAssignments.add(dgua);
        }

        if (guardSchedule.hasVacations()) {
          for (VacationRange vacationRange : guardSchedule.getVacationRanges()) {
            LocalDate startDate = LocalDate.of(year, month, vacationRange.getFrom());
            LocalDate endDate = LocalDate.of(year, month, vacationRange.getTo());
            DateGuardUnityAssignment vdgua = new DateGuardUnityAssignment();
            vdgua.setScheduleAssignmentType(ScheduleAssignmentType.VACATIONAL);
            vdgua.setNumDay(vacationRange.getFrom());
            vdgua.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(startDate.getDayOfWeek()));
            vdgua.setGuardUnityScheduleAssignmentId(gusa.gusaId());
            vdgua.setDate(startDate);
            vdgua.setToDate(endDate);
            vdgua.setHasVacation(false);
            vdgua.setHasExceptions(false);
            vdgua.setFinalized(false);

            processDayOfMonth(month, year, vacationRange.getFrom(), startDate, vdgua);

            createdDateGuardUnityAssignments.add(vdgua);
          }
        }

      }

    }

    dateGuardUnityAssignmentRepository.saveAll(createdDateGuardUnityAssignments);
    return Map.of(
        "scheduleMonthlyId", scheduleMonthly.getId());
  }

  public void processDayOfMonth(Month month, int year, int day, LocalDate date,
      DateGuardUnityAssignment dgua) {
    DayOfMonth dayOfMonth = dayOfMonthRepository.findByMonthAndDayOfMonthAndYear(month, day,
            year)
        .orElseGet(() -> {
          DayOfMonth newDayOfMonth = new DayOfMonth();
          newDayOfMonth.setDayOfMonth(day);
          newDayOfMonth.setMonth(month);
          newDayOfMonth.setYear(year);
          newDayOfMonth.setDayOfWeek(DayOfWeek.fromJavaDayOfWeek(date.getDayOfWeek()));

          WeekOfMonth weekOfMonth = findOrCreateWeek(date);
          newDayOfMonth.setWeekOfMonthId(weekOfMonth.getId());
          return dayOfMonthRepository.save(newDayOfMonth);
        });

    dgua.setDayOfMonthId(dayOfMonth.getId());
  }

  public TurnType getTurnTypeFromShift(String shift) {
    return switch (shift) {
      case "D" -> TurnType.DAY;
      case "N" -> TurnType.NIGHT;

      default -> throw new IllegalStateException("Unexpected value: " + shift);
    };
  }

  public ScheduleAssignmentType getScheduleAssignmentTypeFromShift(String shift) {
    return switch (shift) {
      case "X" -> ScheduleAssignmentType.FREE_DAY;
      case "VC" -> ScheduleAssignmentType.VACATIONAL;
      default -> throw new IllegalStateException("Unexpected value: " + shift);
    };
  }

  public boolean isTurnShift(String shift) {
    return "D".equals(shift) || "N".equals(shift);
  }

  public boolean isScheduleAssignmentShift(String shift) {
    return "X".equals(shift) || "VC".equals(shift);
  }
}
