package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.ContractScheduleSummaryDto;
import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractScheduleUnitTemplateRepository extends
    JpaRepository<ContractScheduleUnitTemplate, Long> {

  @Query("SELECT c.id FROM ContractScheduleUnitTemplate c WHERE c.contractUnity.id = :contractUnityId")
  List<Long> findContractScheduleIdByContractUnityId(Long contractUnityId);

  List<ContractScheduleUnitTemplate> findByContractUnityId(Long contractUnityId);

  @Modifying
  void deleteByContractUnityId(Long contractUnityId);

  @Query("""
        SELECT new com.prd.seccontrol.model.dto.ContractScheduleSummaryDto(
          c.contractUnityId, c.contractUnity.unity.code, t.turnTemplate.turnType, c.dayOfWeek,
                t.id, t.turnTemplate.timeFrom, t.turnTemplate.timeTo
        ) FROM ContractScheduleUnitTemplate c
        INNER JOIN TurnAndHour t ON c.id = t.contractScheduleUnitTemplateId
        WHERE c.contractUnity.clientContractId = :contractClientId
      """)
  List<ContractScheduleSummaryDto> findByContractClientId(Long contractClientId);
}
