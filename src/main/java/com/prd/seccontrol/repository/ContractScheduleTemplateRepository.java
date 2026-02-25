package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ContractScheduleUnitTemplate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractScheduleTemplateRepository extends
    JpaRepository<ContractScheduleUnitTemplate, Long> {

  @Query("SELECT c.id FROM ContractScheduleUnitTemplate c WHERE c.contractUnity.id = :contractUnityId")
  List<Long> findContractScheduleIdByContractUnityId(Long contractUnityId);

  List<ContractScheduleUnitTemplate> findByContractUnityId(Long contractUnityId);

  @Modifying
  void deleteByContractUnityId(Long contractUnityId);
}
