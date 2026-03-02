package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.EmployeeUnitAssignment;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeUnitAssignmentRepository extends JpaRepository<EmployeeUnitAssignment, Long> {
  List<EmployeeUnitAssignment> findByEmployeeAssignmentMonthlyId(Long idLong);

  void deleteByUnityIdNotInAndEmployeeAssignmentMonthlyId(Collection<Long> unityIds, Long employeeAssignmentMonthlyId);

  List<EmployeeUnitAssignment> findByUnityIdIn(Collection<Long> unityIds);
}
