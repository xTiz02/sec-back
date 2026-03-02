package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.EmployeeAssignmentMonthly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAssignmentMonthlyRepository extends
    JpaRepository<EmployeeAssignmentMonthly, Long> {

}
