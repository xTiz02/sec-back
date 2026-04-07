package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardExtraHours;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardExtraHoursRepository extends JpaRepository<GuardExtraHours, Long> {

  Optional<GuardExtraHours> findByPrincipalDateGuardUnityAssignmentId(Long principalDateGuardUnityAssignmentId);

  Optional<GuardExtraHours> findByCoverDateGuardUnityAssignmentId(Long coverDateGuardUnityAssignmentId);

//  Optional<GuardExtraHours> findByGuardAssistanceEventIdAndEndDateIsNull(Long guardAssistanceEventId, LocalDate endDate);


}