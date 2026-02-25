package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ClientContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientContractRepository extends JpaRepository<ClientContract,Long> {

}
