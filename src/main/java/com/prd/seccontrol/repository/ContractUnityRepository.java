package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ContractUnity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractUnityRepository extends JpaRepository<ContractUnity, Long> {

  List<ContractUnity> findByClientContractId(Long clientContractId);
}
