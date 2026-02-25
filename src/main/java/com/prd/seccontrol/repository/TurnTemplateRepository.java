package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.TurnTemplate;
import java.util.List;
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
}
