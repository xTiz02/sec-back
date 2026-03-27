package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ScheduleMonthly;
import java.time.Month;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleMonthlyRepository extends JpaRepository<ScheduleMonthly, Long> {

  ScheduleMonthly findByMonthAndYear(Month month, Integer year);

  @Query("""
      SELECT s FROM ScheduleMonthly s
      WHERE (s.month = :month AND s.year = :year)
         OR (s.month = :yesterdayMonth AND s.year = :yesterdayYear)
    """)
  List<ScheduleMonthly> findByMonthAndYearAndYesterdayDay(Month month, Integer year, Month yesterdayMonth, Integer yesterdayYear);
}
