package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.GuardLiteView;
import com.prd.seccontrol.model.entity.ExternalGuard;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalGuardRepository extends JpaRepository<ExternalGuard,Long> {
  Optional<ExternalGuard> findByUserId(Long userId);

  @Query("""
          SELECT new com.prd.seccontrol.model.dto.GuardLiteView(
                  null,
                  c.id,
                  null,
                  c.documentNumber,
                  c.identificationType,
                  CONCAT(c.firstName, ' ', c.lastName),
                  null
                  ) FROM ExternalGuard c
                  WHERE
                  (c.firstName LIKE CONCAT(:searchTerm, '%')) OR
                  (c.lastName LIKE CONCAT(:searchTerm, '%')) OR
                  (CONCAT(c.firstName, ' ',c.lastName ) LIKE CONCAT(:searchTerm, '%')) OR
                  (c.documentNumber LIKE CONCAT(:searchTerm, '%'))
      """)
  Page<GuardLiteView> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

}
