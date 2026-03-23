package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo;
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

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentIdAndDateIn(
      Long guardUnityScheduleAssignmentId,
      Collection<LocalDate> dates);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentIdAndDateIsBetween(
      Long guardUnityScheduleAssignmentId, LocalDate dateAfter, LocalDate dateBefore);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignment_SpecialServiceUnityScheduleId(
      Long specialServiceUnityScheduleId);


  @Query("update DateGuardUnityAssignment d set d.hasExceptions = :hasExceptions where d.id = :id")
  @Modifying
  void updateHasExceptionsById(boolean hasExceptions, Long id);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.DateGuardUnityAssignmentInfo(
              COALESCE(g.employee.firstName, exg.firstName),
              COALESCE(g.employee.documentNumber, exg.documentNumber),
              g.photoUrl,
              gusa.guardType,
              dgua.id,
              gusa.guardAssignmentId,
              gusa.id,
              dgua.date,
              COALESCE(cu.id, ssu.id),
              COALESCE(cu.unity.name, ssu.unityName),
              COALESCE(cu.unity.direction, ssu.address),
              COALESCE(cu.unity.latitude, ssu.latitude),
              COALESCE(cu.unity.longitude, ssu.longitude),
              COALESCE(cu.unity.rangeCoverage, ssu.rangeCoverage),
              tt,
              dgua.hasExceptions
          )
          FROM DateGuardUnityAssignment dgua
          INNER JOIN dgua.guardUnityScheduleAssignment gusa
          INNER JOIN dgua.turnAndHour.turnTemplate tt
          LEFT JOIN gusa.guardAssignment.externalGuard exg
          LEFT JOIN gusa.guardAssignment.guard g
          LEFT JOIN gusa.contractUnity cu
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
      WHERE dgua.date < :date
      AND NOT EXISTS (
          SELECT 1
          FROM GuardAssistanceEvent g
          WHERE g.dateGuardUnityAssignmentId = dgua.id
          AND g.assistanceType = 1
      )
      ORDER BY dgua.date DESC
      """)
  List<DateGuardUnityAssignment> findLast31WithoutExit(
      @Param("date") LocalDate date,
      Pageable pageable
  );


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
}
