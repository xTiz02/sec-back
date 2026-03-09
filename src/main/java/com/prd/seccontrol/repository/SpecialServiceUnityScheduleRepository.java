package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialServiceUnityScheduleRepository extends
    JpaRepository<SpecialServiceUnitySchedule, Long> {

}
