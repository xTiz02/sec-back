package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ExternalGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalGuardRepository extends JpaRepository<ExternalGuard,Long> {

}
