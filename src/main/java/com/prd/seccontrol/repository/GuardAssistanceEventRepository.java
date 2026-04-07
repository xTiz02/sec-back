package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardAssistanceEvent;
import com.prd.seccontrol.model.types.AssistanceType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardAssistanceEventRepository extends JpaRepository<GuardAssistanceEvent, Long> {
  List<GuardAssistanceEvent> findByDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId);

  List<GuardAssistanceEvent> findByGuardAssignmentId(Long guardAssignmentId);

  List<GuardAssistanceEvent> findTop20ByOrderByCreatedAtDesc();
  Optional<GuardAssistanceEvent> findTopByDateGuardUnityAssignmentIdAndAssistanceType(Long dateGuardUnityAssignmentId, AssistanceType assistanceType);

  @Query("""
  SELECT e FROM GuardAssistanceEvent e
  WHERE e.assistanceType = 1 AND e.dateGuardUnityAssignmentId IN :dateGuardUnityAssignmentIds
""")
  List<GuardAssistanceEvent> findEventsWithExitAssistanceByDateGuardUnityAssignmentIdIn(List<Long> dateGuardUnityAssignmentIds);

  @Query("""
    SELECT gae FROM GuardAssistanceEvent gae
    WHERE gae.dateGuardUnityAssignmentId in :dateGuardUnityAssignmentIds
    """)
  List<GuardAssistanceEvent> findByDateGuardUnityAssignmentIdIn(List<Long> dateGuardUnityAssignmentIds);

  @Query("""
    SELECT gae
    FROM GuardAssistanceEvent gae
    WHERE gae.dateGuardUnityAssignmentId IN :ids
      AND gae.numberOrder = (
          SELECT MAX(gae2.numberOrder)
          FROM GuardAssistanceEvent gae2
          WHERE gae2.dateGuardUnityAssignmentId = gae.dateGuardUnityAssignmentId
      )
""")
  List<GuardAssistanceEvent> findLastEventsByAssignmentIds(List<Long> ids);
}
