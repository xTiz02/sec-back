package com.prd.seccontrol.repository;

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
}
