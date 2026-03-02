package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.DateGuardUnityAssignment;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId);

  @Modifying
  void deleteByGuardUnityScheduleAssignmentId(Long guardUnityScheduleAssignmentId);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentIdAndDateIn(
      Long guardUnityScheduleAssignmentId,
      Collection<LocalDate> dates);

  List<DateGuardUnityAssignment> findByGuardUnityScheduleAssignmentIdAndDateIsBetween(
      Long guardUnityScheduleAssignmentId, LocalDate dateAfter, LocalDate dateBefore);
}
