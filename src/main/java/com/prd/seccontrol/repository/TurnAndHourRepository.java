package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.TurnAndHour;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnAndHourRepository extends JpaRepository<TurnAndHour, Long> {

  List<TurnAndHour> findByContractScheduleUnitTemplateIdIn(Collection<Long> contractScheduleUnitTemplateIds);

  List<TurnAndHour> findByContractScheduleUnitTemplateId(Long contractScheduleUnitTemplateId);

  @Modifying
  void deleteByContractScheduleUnitTemplateId(Long contractScheduleUnitTemplateId);

  @Modifying
  void deleteByContractScheduleUnitTemplateIdAndIdNotIn(Long contractScheduleUnitTemplateId, Collection<Long> contractScheduleUnitTemplateIds);

  @Modifying
  void deleteByContractScheduleUnitTemplateIdIn(Collection<Long> contractScheduleUnitTemplateIds);

  @Query("""
        SELECT t
        FROM TurnAndHour t
        JOIN FETCH t.turnTemplate
        JOIN FETCH t.contractScheduleUnitTemplate
        WHERE t.contractScheduleUnitTemplateId = :id
      """)
  List<TurnAndHour> findFullByScheduleUnitId(@Param("id") Long id);
}
