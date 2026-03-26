package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.TurnTemplate;
import com.prd.seccontrol.model.types.TurnType;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnTemplateRepository extends JpaRepository<TurnTemplate, Long> {

  @Query("""
        SELECT tt
        FROM TurnTemplate tt
        JOIN TurnAndHour tah ON tah.turnTemplateId = tt.id
        WHERE tah.contractScheduleUnitTemplateId = :contractUnitTemplateId
""")
  List<TurnTemplate> findTurnTemplatesByContractUnitTemplateId(Long contractUnitTemplateId);

  @Query("""
    SELECT tt FROM TurnAndHour tah
    JOIN TurnTemplate tt ON tah.turnTemplateId = tt.id
    WHERE tah.id = :turnAndHourId
    """)
  Optional<TurnTemplate> findTurnTemplateByTurnAndHourId(Long turnAndHourId);
  Optional<TurnTemplate> findByTimeFromAndTimeToAndTurnTypeAndNumGuards(LocalTime timeFrom, LocalTime timeTo, TurnType turnType, Integer numGuards);
}
