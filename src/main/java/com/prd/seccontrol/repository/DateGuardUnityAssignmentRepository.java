package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DateGuardUnityAssignmentRepository extends
    JpaRepository<DateGuardUnityAssignment, Long> {
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
              null
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
              null
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
              null
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
              gusa.specialServiceUnityScheduleId
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
      
  WHERE dgua.guardUnityScheduleAssignmentId in :guardUnityAssignmentIds and dgua.date BETWEEN :dateFrom AND :dateTo
  AND dgua.finalized = false AND dgua.toDate is null
  AND dgua.scheduleAssignmentType != 4
  AND se.guardUnityScheduleAssignmentId in :guardUnityAssignmentIds
  
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
              null
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
              null
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
}
