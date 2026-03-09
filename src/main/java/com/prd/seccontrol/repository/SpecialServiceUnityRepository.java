package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.SpecialServiceUnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialServiceUnityRepository extends JpaRepository<SpecialServiceUnity, Long> {

}
