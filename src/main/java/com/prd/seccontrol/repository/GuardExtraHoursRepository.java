package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.GuardExtraHours;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardExtraHoursRepository extends JpaRepository<GuardExtraHours, Long> {

  Optional<GuardExtraHours> findByDateGuardUnityAssignmentId(Long dateGuardUnityAssignmentId);

  Optional<GuardExtraHours> findByGuardAssistanceEventId(Long guardAssistanceEventId);

  Optional<GuardExtraHours> findByGuardAssistanceEventIdAndEndDateIsNull(Long guardAssistanceEventId, LocalDate endDate);
}