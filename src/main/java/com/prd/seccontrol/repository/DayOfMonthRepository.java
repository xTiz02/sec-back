package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.DayOfMonth;
import java.time.Month;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfMonthRepository extends JpaRepository<DayOfMonth, Long> {

  Optional<DayOfMonth> findByMonthAndDayOfMonthAndYear(Month month, Integer dayOfMonth, Integer year);
}
