package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.EventAuditDto;
import com.prd.seccontrol.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Query("""
            SELECT new com.prd.seccontrol.model.dto.EventAuditDto(
              e.id,
              e.method,
              e.uri,
              e.responseStatus,
              e.duration,
              e.startTime,
              COALESCE(CONCAT(emp.firstName, ' ', emp.lastName),CONCAT(ex.firstName, ' ', ex.lastName)),
              COALESCE(emp.documentNumber, ex.documentNumber),
              COALESCE(emp.identificationType, ex.identificationType),
              null,
              e.json
            )
            FROM Event e
            LEFT JOIN Employee emp ON emp.userId = e.userId
            LEFT JOIN ExternalGuard ex ON ex.userId = e.userId
            WHERE e.method IN ('POST','PUT','DELETE')
            AND (
              :term IS NULL OR
              LOWER(CONCAT(emp.firstName, ' ', emp.lastName)) LIKE LOWER(CONCAT('%', :term, '%')) OR
              LOWER(emp.documentNumber) LIKE LOWER(CONCAT('%', :term, '%')) OR
              LOWER(ex.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR
              LOWER(ex.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR
              LOWER(ex.documentNumber) LIKE LOWER(CONCAT('%', :term, '%'))
            )
            ORDER BY e.startTime DESC
      """)
  Page<EventAuditDto> findAuditEvents(String term, Pageable pageable);
}
