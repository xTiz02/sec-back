package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentSimpleInfo;
import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DateGuardUnityAssignmentRepository extends
    JpaRepository<DateGuardUnityAssignment, Long> {

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
              COALESCE(emp.firstName, exg.firstName),
              COALESCE(emp.documentNumber, exg.documentNumber),
              g.photoUrl,
              gusa.guardType,
              dgua.id,
              gusa.guardAssignmentId,
              gusa.id,
              dgua.date,
              COALESCE(cu.id, ssu.id),
              COALESCE(u.name, ssu.unityName),
              COALESCE(u.direction, ssu.address),
              COALESCE(u.latitude, ssu.latitude),
              COALESCE(u.longitude, ssu.longitude),
              COALESCE(u.rangeCoverage, ssu.rangeCoverage),
              dgua.turnAndHour.turnTemplate,
              dgua.hasExceptions
          )
          FROM DateGuardUnityAssignment dgua
          INNER JOIN dgua.guardUnityScheduleAssignment gusa
          LEFT JOIN gusa.guardAssignment ga
          LEFT JOIN ga.externalGuard exg
          LEFT JOIN ga.guard g
          LEFT JOIN g.employee emp
          LEFT JOIN gusa.contractUnity cu
          LEFT JOIN cu.unity u
          LEFT JOIN gusa.specialServiceUnitySchedule ssus
          LEFT JOIN ssus.specialServiceUnity ssu
          WHERE dgua.id IN :ids
      """)
  List<DateGuardUnityAssignmentInfo> findAllDateGuardUnityAssignmentInfo(
      @Param("ids") Collection<Long> ids
  );


  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo(
              gusa.guardType,
              dgua.id,
              gusa.guardAssignmentId,
              gusa.id,
              tt,
              dgua.hasExceptions
          )
          FROM DateGuardUnityAssignment dgua
          INNER JOIN dgua.guardUnityScheduleAssignment gusa
          INNER JOIN dgua.turnAndHour.turnTemplate tt
          WHERE dgua.id = :dateGuardUnityAssignmentId
      """)
  Optional<DateGuardUnityAssignmentInfo> findDateGuardUnityAssignmentInfoById(
      Long dateGuardUnityAssignmentId);


  @Query("""
  SELECT dgua.id
  FROM DateGuardUnityAssignment dgua
  LEFT JOIN ScheduleException se
      ON se.dateGuardUnityAssignmentId = dgua.id
  LEFT JOIN dgua.guardUnityScheduleAssignment gusa
  LEFT JOIN gusa.guardAssignment ga
  LEFT JOIN ga.guard g
  LEFT JOIN ga.externalGuard exg
  
  LEFT JOIN se.guardUnityScheduleAssignment gusaEx
  LEFT JOIN gusaEx.guardAssignment gaEx
  LEFT JOIN gaEx.guard gEx
  LEFT JOIN gaEx.externalGuard exgEx
  
  WHERE dgua.date <= :date
  AND (
      (
          dgua.hasExceptions = true AND
          (
              (:guardId IS NOT NULL AND gEx.id = :guardId) OR
              (:externalGuardId IS NOT NULL AND exgEx.id = :externalGuardId)
          )
      )
      OR
      (
          dgua.hasExceptions = false AND
          (
              (:guardId IS NOT NULL AND g.id = :guardId) OR
              (:externalGuardId IS NOT NULL AND exg.id = :externalGuardId)
          )
      )
  )
  ORDER BY dgua.date DESC
""")
  Page<Long> findLastDateGuardUnityAssignmentIds(
      @Param("guardId") Long guardId,
      @Param("externalGuardId") Long externalGuardId,
      @Param("date") LocalDate date,
      Pageable pageable
  );

  @Query("""
          SELECT DISTINCT g.dateGuardUnityAssignmentId
          FROM GuardAssistanceEvent g
          WHERE g.dateGuardUnityAssignmentId IN :ids
          AND g.assistanceType = 1
          AND NOT EXISTS (
              SELECT 1
              FROM GuardExtraHours geh
              WHERE geh.guardAssistanceEventId = g.id
              AND geh.endDate IS NULL
      )
      """)
  Set<Long> findDateGuardIdsWithExitWithoutOpenExtraHours(
      @Param("ids") Set<Long> ids
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
