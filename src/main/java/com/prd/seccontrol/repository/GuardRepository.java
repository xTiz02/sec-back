package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.GuardLiteProjection;
import com.prd.seccontrol.model.dto.GuardLiteView;
import com.prd.seccontrol.model.entity.Guard;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardRepository extends JpaRepository<Guard, Long> {

  Optional<Guard> findByEmployee_UserId(Long employeeUserId);

  @Query("""
      SELECT g.id, g.code, g.guardType FROM Guard g
      WHERE g.code IN :codes
      """)
  List<Object[]> findGuardIdsByCodeIn(Collection<String> codes);

//  public record GuardLiteView(
//      Long guardId,
//      Long externalGuardId,
//      String guardCode,
//      String documentNumber,
//      String guardName,
//      GuardType guardType
//  ) {
  @Query("""
          SELECT new com.prd.seccontrol.model.dto.GuardLiteView(
                  c.id,
                  null,
                  c.code,
                  c.employee.documentNumber,
                  c.employee.identificationType,
                  CONCAT(c.employee.firstName, ' ', c.employee.lastName),
                  c.guardType
                  ) FROM Guard c
                  WHERE 
                  (c.employee.firstName LIKE CONCAT(:searchTerm, '%')) OR 
                  (c.employee.lastName LIKE CONCAT(:searchTerm, '%')) OR 
                  (CONCAT(c.employee.firstName, ' ',c.employee.lastName ) LIKE CONCAT(:searchTerm, '%')) OR 
                  (c.employee.documentNumber LIKE CONCAT(:searchTerm, '%'))
      """)
  Page<GuardLiteView> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);


  @Query(value = """
    SELECT 
        c.id as guardId,
        null as externalGuardId,
        c.code as guardCode,
        e.document_number as documentNumber,
        e.identification_type as identificationType,
        CONCAT(e.first_name, ' ', e.last_name) as guardName,
        c.guard_type as guardType
    FROM guard c
    JOIN employee e ON c.employee_id = e.id
    WHERE 
        e.first_name LIKE CONCAT(:searchTerm, '%') OR
        e.last_name LIKE CONCAT(:searchTerm, '%') OR
        CONCAT(e.first_name, ' ', e.last_name) LIKE CONCAT(:searchTerm, '%') OR
        e.document_number LIKE CONCAT(:searchTerm, '%')

    UNION

    SELECT 
        null as guardId,
        c.id as externalGuardId,
        null as guardCode,
        c.document_number as documentNumber,
        c.identification_type as identificationType,
        CONCAT(c.first_name, ' ', c.last_name) as guardName,
        null as guardType
    FROM external_guard c
    WHERE 
        c.first_name LIKE CONCAT(:searchTerm, '%') OR
        c.last_name LIKE CONCAT(:searchTerm, '%') OR
        CONCAT(c.first_name, ' ', c.last_name) LIKE CONCAT(:searchTerm, '%') OR
        c.document_number LIKE CONCAT(:searchTerm, '%')
""",
      countQuery = """
    SELECT COUNT(*) FROM (
        SELECT c.id
        FROM guard c
        JOIN employee e ON c.employee_id = e.id
        WHERE 
            e.first_name LIKE CONCAT(:searchTerm, '%') OR
            e.last_name LIKE CONCAT(:searchTerm, '%') OR
            CONCAT(e.first_name, ' ', e.last_name) LIKE CONCAT(:searchTerm, '%') OR
            e.document_number LIKE CONCAT(:searchTerm, '%')

        UNION

        SELECT c.id
        FROM external_guard c
        WHERE 
            c.first_name LIKE CONCAT(:searchTerm, '%') OR
            c.last_name LIKE CONCAT(:searchTerm, '%') OR
            CONCAT(c.first_name, ' ', c.last_name) LIKE CONCAT(:searchTerm, '%') OR
            c.document_number LIKE CONCAT(:searchTerm, '%')
    ) as total
""",
      nativeQuery = true)
  Page<GuardLiteProjection> findAllUnified(@Param("searchTerm") String searchTerm, Pageable pageable);
}
