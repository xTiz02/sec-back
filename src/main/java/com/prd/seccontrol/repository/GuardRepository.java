package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.Guard;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardRepository extends JpaRepository<Guard, Long> {

  Optional<Guard> findByEmployee_UserId(Long employeeUserId);

  @Query("""
  SELECT g.id, g.code, g.guardType FROM Guard g
  WHERE g.code IN :codes
  """)
  List<Object[]> findGuardIdsByCodeIn(Collection<String> codes);
}
