package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.SpecialServiceUnitySchedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialServiceUnityScheduleRepository extends
    JpaRepository<SpecialServiceUnitySchedule, Long> {

  @Query("""
    SELECT s FROM SpecialServiceUnitySchedule s
    WHERE :date between  s.dateFrom and  s.dateTo
    """)
  List<SpecialServiceUnitySchedule> findByDateBetween(LocalDate date);
}
