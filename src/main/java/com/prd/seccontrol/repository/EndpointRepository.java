package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint,Long> {

}
