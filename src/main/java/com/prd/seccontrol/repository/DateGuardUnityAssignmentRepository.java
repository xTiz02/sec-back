package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo;
import com.prd.seccontrol.model.dto.GuardShiftBaseDTO;
import com.prd.seccontrol.model.dto.NextShiftToCoverDto;
import com.prd.seccontrol.model.dto.ShiftProjection;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import com.prd.seccontrol.model.types.ScheduleAssignmentType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DateGuardUnityAssignmentRepository extends
    JpaRepository<DateGuardUnityAssignment, Long> {

  @Query("""
    SELECT new com.prd.seccontrol.model.dto.NextShiftToCoverDto(
    dgua.id,
    dgua.date,
    COALESCE(CONCAT(e.firstName, ' ', e.lastName), CONCAT(eg.firstName, ' ', eg.lastName)),
    COALESCE(g.code, eg.documentNumber),
    COALESCE(e.documentNumber, eg.documentNumber),
    COALESCE(g.employee.identificationType, eg.identificationType),
    dgua.dateTimeEntry,
    dgua.dateTimeEnd,
    tt.name,
    tt.turnType
    ) FROM DateGuardUnityAssignment dgua
    JOIN dgua.guardUnityScheduleAssignment gusa
    JOIN gusa.scheduleMonthly sm
    JOIN dgua.turnAndHour tah
    JOIN tah.turnTemplate tt
    LEFT JOIN gusa.guardAssignment ga
    LEFT JOIN ga.guard g
    LEFT JOIN g.employee e
    LEFT JOIN ga.externalGuard eg
    WHERE gusa.contractUnityId = :contractUnityId
    AND (gusa.specialServiceUnityScheduleId = :specialServiceId OR :specialServiceId IS NULL)
    AND dgua.dayOfMonthId IN :nextDayShiftIds
    AND dgua.finalized = false
    AND dgua.toDate IS NULL
    AND dgua.scheduleAssignmentType != 4
    AND (
        dgua.dateTimeEntry >= :maxExitPrincipalTime
        AND dgua.dateTimeEntry <= :maxEntryNextDayTime
    )
    
""")
  List<NextShiftToCoverDto> findNextShiftsToCover(
      Long contractUnityId, Long specialServiceId, List<Long> nextDayShiftIds,
       LocalDateTime maxExitPrincipalTime, LocalDateTime maxEntryNextDayTime);
  @Transactional
  @Modifying
  @Query("""
          UPDATE DateGuardUnityAssignment dgua
          SET dgua.scheduleAssignmentType = :scheduleAssignmentType, 
              dgua.finalized = true
          WHERE dgua.id = :dateGuardUnityAssignmentId
  """)
  void updateAbsentScheduleAssistanceById(ScheduleAssignmentType scheduleAssignmentType, Long dateGuardUnityAssignmentId);

  @Modifying
  @Query("""
          UPDATE DateGuardUnityAssignment dgua
          SET dgua.hasExtraHours = :hasExtraHours,
              dgua.finalized = :finalized
          WHERE dgua.id = :dateGuardUnityAssignmentId
  """)
  void updateHasExtraHoursById(Long dateGuardUnityAssignmentId, boolean hasExtraHours, boolean finalized);

  @Query(value = """
    SELECT 
        dgua.id as id,
        DATE_FORMAT(dgua.date, '%Y-%m-%d') as date,

        /* Guard info */
        COALESCE(
            CONCAT(e.first_name, ' ', e.last_name),
            CONCAT(eg.first_name, ' ', eg.last_name)
        ) as guardName,

        g.code as guardCode,

        COALESCE(e.document_number, eg.document_number) as documentNumber,

        gusa.guard_type as guardType,

        CASE 
            WHEN ga.external_guard_id IS NOT NULL THEN true 
            ELSE false 
        END as isExternalGuard,

        /* Unity */
        u.name as contractUnityName,
        ssu.unity_name as specialServiceUnityName,

        c.name as clientName,

        /* Horarios desde DateGuardUnityAssignment */
        TIME_FORMAT(dgua.date_time_entry, '%H:%i') as scheduledEntry,
        TIME_FORMAT(dgua.date_time_end, '%H:%i') as scheduledExit,

        dgua.schedule_assignment_type as scheduleAssignmentType,

        dgua.has_exceptions as hasExceptions,
        dgua.has_extra_hours as hasExtraHours,
        dgua.has_marks as hasMarks,
        dgua.has_vacation as hasVacation,
        dgua.finalized as finalized

    FROM date_guard_unity_assignment dgua

    LEFT JOIN guard_unity_schedule_assignment gusa 
        ON dgua.guard_unity_schedule_assignment_id = gusa.id

    LEFT JOIN guard_assignment ga 
        ON gusa.guard_assignment_id = ga.id

    LEFT JOIN guard g 
        ON ga.guard_id = g.id

    LEFT JOIN employee e 
        ON g.employee_id = e.id

    LEFT JOIN external_guard eg 
        ON ga.external_guard_id = eg.id

    LEFT JOIN contract_unity cu 
        ON gusa.contract_unity_id = cu.id

    LEFT JOIN unity u 
        ON cu.unity_id = u.id

    LEFT JOIN client c 
        ON u.client_id = c.id

    LEFT JOIN special_service_unity_schedule sss 
        ON gusa.special_service_unity_schedule_id = sss.id

    LEFT JOIN special_service_unity ssu 
        ON sss.special_service_unity_id = ssu.id

    WHERE
        (:dateFrom IS NULL OR dgua.date >= :dateFrom)
        AND (:dateTo IS NULL OR dgua.date <= :dateTo)

        AND (:guardId IS NULL OR ga.guard_id = :guardId)
        AND (:externalGuardId IS NULL OR ga.external_guard_id = :externalGuardId)

        AND (:unityId IS NULL OR cu.unity_id = :unityId)
        AND (:specialServiceUnityId IS NULL OR sss.special_service_unity_id = :specialServiceUnityId)

        AND (:scheduleAssignmentType IS NULL OR dgua.schedule_assignment_type = :scheduleAssignmentType)

        AND (:hasExceptions IS NULL OR dgua.has_exceptions = :hasExceptions)
        AND (:hasExtraHours IS NULL OR dgua.has_extra_hours = :hasExtraHours)
        AND (:hasMarks IS NULL OR dgua.has_marks = :hasMarks)

        AND (:clientId IS NULL OR u.client_id = :clientId)
""",
      countQuery = """
    SELECT COUNT(*) 
    FROM date_guard_unity_assignment dgua

    LEFT JOIN guard_unity_schedule_assignment gusa 
        ON dgua.guard_unity_schedule_assignment_id = gusa.id

    LEFT JOIN guard_assignment ga 
        ON gusa.guard_assignment_id = ga.id

    LEFT JOIN contract_unity cu 
        ON gusa.contract_unity_id = cu.id

    LEFT JOIN unity u 
        ON cu.unity_id = u.id

    LEFT JOIN special_service_unity_schedule sss 
        ON gusa.special_service_unity_schedule_id = sss.id

    WHERE
        (:dateFrom IS NULL OR dgua.date >= :dateFrom)
        AND (:dateTo IS NULL OR dgua.date <= :dateTo)

        AND (:guardId IS NULL OR ga.guard_id = :guardId)
        AND (:externalGuardId IS NULL OR ga.external_guard_id = :externalGuardId)

        AND (:unityId IS NULL OR cu.unity_id = :unityId)
        AND (:specialServiceUnityId IS NULL OR sss.special_service_unity_id = :specialServiceUnityId)

        AND (:scheduleAssignmentType IS NULL OR dgua.schedule_assignment_type = :scheduleAssignmentType)

        AND (:hasExceptions IS NULL OR dgua.has_exceptions = :hasExceptions)
        AND (:hasExtraHours IS NULL OR dgua.has_extra_hours = :hasExtraHours)
        AND (:hasMarks IS NULL OR dgua.has_marks = :hasMarks)

        AND (:clientId IS NULL OR u.client_id = :clientId)
""",
      nativeQuery = true)
  Page<ShiftProjection> searchShifts(
      @Param("dateFrom") LocalDate dateFrom,
      @Param("dateTo") LocalDate dateTo,
      @Param("clientId") Long clientId,
      @Param("guardId") Long guardId,
      @Param("externalGuardId") Long externalGuardId,
      @Param("unityId") Long unityId,
      @Param("specialServiceUnityId") Long specialServiceUnityId,
      @Param("scheduleAssignmentType") String scheduleAssignmentType,
      @Param("hasExceptions") Boolean hasExceptions,
      @Param("hasExtraHours") Boolean hasExtraHours,
      @Param("hasMarks") Boolean hasMarks,
      Pageable pageable
  );

  @Query("""
    SELECT new com.prd.seccontrol.model.dto.GuardShiftBaseDTO(
        dg.id,
        COALESCE(g.employee.firstName, eg.firstName),
        COALESCE(g.code, eg.documentNumber),
        COALESCE(g.employee.documentNumber, eg.documentNumber),
        gus.guardType,
        dg.scheduleAssignmentType,
        dg.dateTimeEntry,
        dg.dateTimeEnd,
        dg.hasExceptions,
        dg.hasExtraHours,
        dg.hasMarks
    )
    FROM DateGuardUnityAssignment dg
    JOIN dg.guardUnityScheduleAssignment gus
    JOIN gus.guardAssignment ga
    LEFT JOIN ga.guard g
    LEFT JOIN ga.externalGuard eg
    WHERE (dg.date = :date OR :dateTime between dg.dateTimeEntry AND dg.dateTimeEnd)
    AND gus.contractUnityId = :contractUnityId
      AND gus.scheduleMonthlyId = :scheduleMonthlyId
""")
  List<GuardShiftBaseDTO> getShiftsBase(Long contractUnityId, Long scheduleMonthlyId, LocalDateTime dateTime, LocalDate date);

//filtrar por rango de fecha y los qeu no estan cerrados
  @Query("""
    SELECT dgua FROM DateGuardUnityAssignment dgua
    WHERE dgua.finalized = false and dgua.hasExtraHours = false and dgua.toDate is null and dgua.scheduleAssignmentType != 3
         and (dgua.dateTimeEntry <= :today or dgua.dateTimeEnd >= :today)
    """)
  List<DateGuardUnityAssignment> findLastActiveShifts(LocalDateTime today);

  @Query(
      """
                SELECT dgua FROM DateGuardUnityAssignment dgua
                    JOIN FETCH dgua.guardUnityScheduleAssignment gusa
                    JOIN FETCH gusa.scheduleMonthly sm
                    WHERE gusa.contractUnityId = :contractUnityId AND sm.id = :scheduleMonthlyId
          """
  )
  List<DateGuardUnityAssignment> findByContractUnityIdAndScheduleMonthlyId(Long contractUnityId,
      Long scheduleMonthlyId);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentId(
      Long guardUnityScheduleAssignmentId);

  @Modifying
  void deleteByGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              null,
              null,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            WHERE dgua.guardUnityScheduleAssignmentId = :guardUnityScheduleAssignmentId
            AND dgua.date IN :dates
  """)
  List<DateGuardUnityAssignmentSimpleInfo> findByGuardUnityScheduleAssignmentIdAndDateIn(
      Long guardUnityScheduleAssignmentId,
      Collection<LocalDate> dates);
  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              null,
              null,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            WHERE dgua.guardUnityScheduleAssignmentId = :guardUnityScheduleAssignmentId
            AND dgua.date BETWEEN :dateAfter AND :dateBefore
  """)
  List<DateGuardUnityAssignmentSimpleInfo> findByGuardUnityScheduleAssignmentIdAndDateIsBetween(
      Long guardUnityScheduleAssignmentId, LocalDate dateAfter, LocalDate dateBefore);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              null,
              null,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            WHERE dgua.id = :id
  """)
  Optional<DateGuardUnityAssignmentSimpleInfo> findDtoById(
      Long id);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              gusa.contractUnityId,
              gusa.specialServiceUnityScheduleId,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            inner join dgua.guardUnityScheduleAssignment gusa
            WHERE dgua.id = :id
  """)
  Optional<DateGuardUnityAssignmentSimpleInfo> findDtoByIdV2(
      Long id);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignment_SpecialServiceUnityScheduleId(
      Long specialServiceUnityScheduleId);


  @Query("update DateGuardUnityAssignment d set d.hasExceptions = :hasExceptions where d.id = :id")
  @Modifying
  void updateHasExceptionsById(boolean hasExceptions, Long id);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo(
              dgua.id,
              dgua.date,
              dgua.turnAndHourId,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized
          )
          FROM DateGuardUnityAssignment dgua
          WHERE dgua.id IN :ids
      """)
  List<DateGuardUnityAssignmentInfo> findAllDateGuardUnityAssignmentInfo(
      @Param("ids") Collection<Long> ids
  );


  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo(
              dgua.date,
              gusa.guardType,
              dgua.id,
              gusa.guardAssignmentId,
              gusa.id,
              se.guardUnityScheduleAssignmentId,
              tt,
              dgua.turnAndHourId,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized
          )
          FROM DateGuardUnityAssignment dgua
          INNER JOIN dgua.guardUnityScheduleAssignment gusa
          INNER JOIN dgua.turnAndHour.turnTemplate tt
          LEFT JOIN ScheduleException se ON se.dateGuardUnityAssignmentId = dgua.id
          WHERE dgua.id = :dateGuardUnityAssignmentId
      """)
  Optional<DateGuardUnityAssignmentInfo> findDateGuardUnityAssignmentInfoById(
      Long dateGuardUnityAssignmentId);

  @Query("""
      SELECT dgua.id, tt, dgua.date, dgua.guardUnityScheduleAssignmentId, se.guardUnityScheduleAssignmentId
      FROM DateGuardUnityAssignment dgua
      LEFT JOIN dgua.turnAndHour tah
      LEFT JOIN tah.turnTemplate tt
      LEFT JOIN ScheduleException se
          ON se.dateGuardUnityAssignmentId = dgua.id
      
      WHERE dgua.guardUnityScheduleAssignmentId IN :guardUnityAssignmentIds
      AND dgua.date BETWEEN :dateFrom AND :dateTo
      AND dgua.finalized = false
      AND dgua.toDate IS NULL
      AND dgua.scheduleAssignmentType != 4
      AND (
          se.guardUnityScheduleAssignmentId IS NULL
          OR se.guardUnityScheduleAssignmentId IN :guardUnityAssignmentIds
      )
      ORDER BY dgua.date DESC
      """)
  List<Object[]> findLastDateGuardUnityAssignmentIds(
      @Param("dateFrom") LocalDate dateFrom,
      @Param("dateTo") LocalDate dateTo,
      @Param("guardUnityAssignmentIds") Collection<Long> guardUnityAssignmentIds
  );

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              null,
              null,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            WHERE dgua.guardUnityScheduleAssignmentId = :guardUnityScheduleAssignmentId
            AND dgua.date = :date AND dgua.finalized = false
  """)
  List<DateGuardUnityAssignmentSimpleInfo> findByGuardUnityScheduleAssignmentIdAndDate(Long guardUnityScheduleAssignmentId, LocalDate date);



  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo(
              dgua.id,
              dgua.dayOfMonthId,
              dgua.guardUnityScheduleAssignmentId,
              dgua.turnAndHourId,
              dgua.dayOfWeek,
              dgua.numDay,
              dgua.date,
              dgua.scheduleAssignmentType,
              dgua.toDate,
              dgua.hasVacation,
              dgua.hasExceptions,
              dgua.hasExtraHours,
              dgua.finalized,
              dgua.createdAt,
              dgua.updatedAt,
              null,
              null,
              dgua.dateTimeEntry,
              dgua.dateTimeEnd
          ) from DateGuardUnityAssignment dgua
            WHERE dgua.guardUnityScheduleAssignmentId = :guardUnityScheduleAssignmentId
  """)
  List<DateGuardUnityAssignmentSimpleInfo> findDtoByGuardUnityScheduleAssignment(Long guardUnityScheduleAssignmentId);
  @Modifying
  @Query("""
          UPDATE DateGuardUnityAssignment dgua
          SET dgua.finalized = :finalized
          WHERE dgua.id IN :ids
  """)
  void updateFinalizedByIds(@Param("finalized") boolean finalized, @Param("ids") List<Long> ids);

  @Modifying
  @Query("""
          UPDATE DateGuardUnityAssignment dgua
          SET dgua.hasMarks = :hasMarks
          WHERE dgua.id = :id
  """)
  void updateHasMarksById(@Param("hasMarks") boolean hasMarks, @Param("id") Long id);
}
