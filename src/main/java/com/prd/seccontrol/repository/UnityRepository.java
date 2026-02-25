package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.Unity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnityRepository extends JpaRepository<Unity,Long> {

}
