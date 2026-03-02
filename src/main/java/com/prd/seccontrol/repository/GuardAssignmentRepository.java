package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardAssignmentRepository extends JpaRepository<GuardAssignment, Long> {

}
