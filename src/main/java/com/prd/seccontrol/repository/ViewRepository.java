package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends JpaRepository<View,Long> {

}
