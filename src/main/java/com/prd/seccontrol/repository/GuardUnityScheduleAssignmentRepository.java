package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.GuardUnityScheduleSummaryDto;
import com.prd.seccontrol.model.dto.UnitMonitoringStatusResume;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardUnityScheduleAssignmentRepository extends
    JpaRepository<GuardUnityScheduleAssignment, Long> {

  Long countByScheduleMonthlyId(Long scheduleMonthlyId);

//  Long contractUnityId,
//  String unityName,
//  String unityCode,
//  String clientName,
//  String clientContractName,
//  String address,
//  Double latitude,
//  Double longitude,
//  LocalDate date,
//  Integer totalShifts,
//  Integer arrivedGuards,
//  Integer absentGuards
//  // todo aligerar esto
  @Query(value = """
    SELECT 
        cu.id AS contractUnityId,
        u.name AS unityName,
        u.code AS unityCode,
        c.name AS clientName,
        cc.name AS clientContractName,
        u.direction AS address,
        u.latitude AS latitude,
        u.longitude AS longitude,
        dg.date AS date,
        COUNT(dg.id) AS totalShifts,
        SUM(CASE WHEN dg.has_marks = true THEN 1 ELSE 0 END) AS arrivedGuards,
        SUM(CASE WHEN dg.has_marks = false THEN 1 ELSE 0 END) AS absentGuards
    FROM guard_unity_schedule_assignment gus
    JOIN contract_unity cu ON cu.id = gus.contract_unity_id
    JOIN unity u ON u.id = cu.unity_id
    JOIN client_contract cc ON cc.id = cu.client_contract_id
    JOIN client c ON c.id = cc.client_id
    LEFT JOIN date_guard_unity_assignment dg 
        ON dg.guard_unity_schedule_assignment_id = gus.id
    WHERE gus.schedule_monthly_id = :scheduleMonthlyId
      AND gus.contract_unity_id IS NOT NULL
      AND (
        dg.date = :date 
        OR :dateTime BETWEEN dg.date_time_entry AND dg.date_time_end
      )
    GROUP BY 
        cu.id,
        u.name,
        u.code,
        c.name,
        cc.name,
        u.direction,
        u.latitude,
        u.longitude,
        dg.date
""", nativeQuery = true)
  List<UnitMonitoringStatusResume> getAssignmentCountByScheduleMonthlyId(
      Long scheduleMonthlyId,
      LocalDateTime dateTime,
      LocalDate date
  );
  List<GuardUnityScheduleAssignment> findByContractUnityIdAndScheduleMonthlyId(Long contractUnityId,
      Long scheduleMonthlyId);

  List<GuardUnityScheduleAssignment> findByScheduleMonthlyId(Long scheduleMonthlyId);

  @Query("""
      SELECT gus FROM GuardUnityScheduleAssignment gus
      WHERE (gus.guardAssignment.guardId = :guardId
          OR gus.guardAssignment.externalGuardId = :externalGuardId)
      AND gus.specialServiceUnityScheduleId = :specialServiceUnityScheduleId
      """)
  Optional<GuardUnityScheduleAssignment> findByGuardIdAndSpecialServiceUnityScheduleId(
      Long guardId,
      Long externalGuardId,
      Long specialServiceUnityScheduleId
  );

  @Query("""
      SELECT new com.prd.seccontrol.model.dto.GuardUnityScheduleSummaryDto(
            gus.id,gus.guardAssignment.guardId, gus.guardAssignment.guard.code, gus.contractUnityId,gus.contractUnity.unity.code,
                  gus.guardAssignment.guard.guardType ) FROM GuardUnityScheduleAssignment gus
      WHERE gus.guardAssignment.guard.id IN :guardIds AND gus.contractUnityId in :contractUnityIds
      AND gus.scheduleMonthlyId = :scheduleMonthlyId
      """)
  List<GuardUnityScheduleSummaryDto> findIdsByGuardIdAndScheduleMonthlyId(
      List<Long> guardIds,
      Long scheduleMonthlyId,
      List<Long> contractUnityIds
  );

  @Query("""
      SELECT gus FROM GuardUnityScheduleAssignment gus
      WHERE (gus.guardAssignment.guardId = :guardId
          OR gus.guardAssignment.externalGuardId = :externalGuardId)
      AND gus.scheduleMonthlyId = :scheduleMonthlyId
      """)
  Optional<GuardUnityScheduleAssignment> findByGuardIdAndScheduleMonthlyId(
      Long guardId,
      Long externalGuardId,
      Long scheduleMonthlyId
  );

  @Query("""
      SELECT gus FROM GuardUnityScheduleAssignment gus
      INNER JOIN gus.guardAssignment ga
      WHERE
      (
          (:monthlyIds IS NULL OR gus.scheduleMonthlyId IN :monthlyIds)
          OR
          (:specialServiceUnityScheduleIds IS NULL OR gus.specialServiceUnityScheduleId IN :specialServiceUnityScheduleIds)
      )
      AND
      (:guardId IS NULL OR ga.guardId = :guardId)
      AND
      (:externalGuardId IS NULL OR ga.externalGuardId = :externalGuardId)
      """)
  List<GuardUnityScheduleAssignment> findByGuardIdAndScheduleMonthlyIdsOrSpecialServiceUnityScheduleIds(
      List<Long> monthlyIds,
      List<Long> specialServiceUnityScheduleIds,
      Long guardId,
      Long externalGuardId
  );


}
