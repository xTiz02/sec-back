package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardRequest;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardRequestRepository extends JpaRepository<GuardRequest, Long> {

  List<GuardRequest> findByGuardAssistanceEventIdIn(Collection<Long> guardAssistanceEventIds);
}
