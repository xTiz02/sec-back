package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ScheduleException;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExceptionRepository extends JpaRepository<ScheduleException, Long> {

  List<ScheduleException> findByDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId);

  List<ScheduleException> findByDateGuardUnityAssignmentIdIn(Collection<Long> dateGuardUnityAssignmentIds);
}
