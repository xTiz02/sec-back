package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.SecurityProfile;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityProfileRepository extends JpaRepository<SecurityProfile,Long> {

  Set<SecurityProfile> findByIdIn(Collection<Long> ids);
}
