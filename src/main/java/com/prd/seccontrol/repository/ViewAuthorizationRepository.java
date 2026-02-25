package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.ViewAuthorization;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewAuthorizationRepository extends JpaRepository<ViewAuthorization, Long> {

  List<ViewAuthorization> findBySecurityProfileId(Long id);

  @Query("""
      SELECT DISTINCT va
      FROM ViewAuthorization va
      WHERE va.securityProfileId IN (:securityProfileIdSet)
      ORDER BY va.viewId ASC
      """)
  List<ViewAuthorization> findViewBySecurityProfileId(
      @Param("securityProfileIdSet") Set<Long> securityProfileIdSet
  );

  List<ViewAuthorization> findBySecurityProfileIdAndViewIdIn(
      Long securityProfileId,
      List<Long> viewIds
  );

  @Modifying
  void deleteBySecurityProfileIdAndIdNotIn(Long id, Set<Long> viewAuthorizationIdSet);

  @Modifying
  void deleteBySecurityProfileId(Long id);
}
