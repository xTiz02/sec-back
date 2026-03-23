package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.Guard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardRepository extends JpaRepository<Guard, Long> {

  Optional<Guard> findByEmployee_UserId(Long employeeUserId);
}
