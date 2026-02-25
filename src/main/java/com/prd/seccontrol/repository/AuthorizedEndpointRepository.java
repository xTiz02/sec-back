package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.AuthorizedEndpoint;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizedEndpointRepository extends JpaRepository<AuthorizedEndpoint,Long> {

  List<AuthorizedEndpoint> findBySecurityProfileIdAndEndpointIdIn(Long securityProfileId, Collection<Long> endpointIds);

  @Modifying
  void deleteBySecurityProfileIdAndIdNotIn(Long id, Set<Long> authorizedEndpointIdSet);
  @Modifying
  void deleteBySecurityProfileId(Long id);
}
