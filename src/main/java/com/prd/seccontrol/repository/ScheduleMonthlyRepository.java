package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ScheduleMonthly;
import java.time.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleMonthlyRepository extends JpaRepository<ScheduleMonthly, Long> {

  ScheduleMonthly findByMonthAndYear(Month month, Integer year);
}
