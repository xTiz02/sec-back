package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.GuardUnityScheduleSummaryDto;
import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardUnityScheduleAssignmentRepository extends
    JpaRepository<GuardUnityScheduleAssignment, Long> {

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
            gus.id,gus.guardAssignment.guardId, gus.guardAssignment.guard.code,
                  gus.guardAssignment.guard.guardType ) FROM GuardUnityScheduleAssignment gus
      WHERE gus.guardAssignment.guard.id IN :guardIds
      AND gus.scheduleMonthlyId = :scheduleMonthlyId
      """)
  List<GuardUnityScheduleSummaryDto> findIdsByGuardIdAndScheduleMonthlyId(
      List<Long> guardIds,
      Long scheduleMonthlyId
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
