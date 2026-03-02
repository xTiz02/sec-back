package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardUnityScheduleAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardUnityScheduleAssignmentRepository extends
    JpaRepository<GuardUnityScheduleAssignment, Long> {

  List<GuardUnityScheduleAssignment> findByContractUnityIdAndScheduleMonthlyId(Long contractUnityId, Long scheduleMonthlyId);

}
