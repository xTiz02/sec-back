package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.WeekOfMonth;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekOfMonthRepository extends JpaRepository<WeekOfMonth, Integer> {

  Optional<WeekOfMonth> findByDateFromAndDateTo(LocalDate dateFrom, LocalDate dateTo);
}
