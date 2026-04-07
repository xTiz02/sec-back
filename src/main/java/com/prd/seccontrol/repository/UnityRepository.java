package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.dto.UnityLiteProjection;
import com.prd.seccontrol.model.entity.Unity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnityRepository extends JpaRepository<Unity,Long> {

  @Query(value = """
    SELECT 
        u.id as unityId,
        null as specialServiceUnityId,
        u.name as unityName,
        u.code as unityCode,
        u.direction as address,
        c.name as clientName
    FROM unity u
    LEFT JOIN client c ON u.client_id = c.id
    WHERE 
        u.name LIKE CONCAT(:term, '%') OR
        u.code LIKE CONCAT(:term, '%') OR
        c.name LIKE CONCAT(:term, '%')

    UNION

    SELECT 
        null as unityId,
        s.id as specialServiceUnityId,
        s.unity_name as unityName,
        s.code as unityCode,
        s.address as address,
        null as clientName
    FROM special_service_unity s
    WHERE 
        s.unity_name LIKE CONCAT(:term, '%') OR
        s.code LIKE CONCAT(:term, '%') OR
        s.address LIKE CONCAT(:term, '%')
""",
      countQuery = """
    SELECT COUNT(*) FROM (
        SELECT u.id
        FROM unity u
        LEFT JOIN client c ON u.client_id = c.id
        WHERE 
            u.name LIKE CONCAT(:term, '%') OR
            u.code LIKE CONCAT(:term, '%') OR
            c.name LIKE CONCAT(:term, '%')

        UNION

        SELECT s.id
        FROM special_service_unity s
        WHERE 
            s.unity_name LIKE CONCAT(:term, '%') OR
            s.code LIKE CONCAT(:term, '%') OR
            s.address LIKE CONCAT(:term, '%')
    ) as total
""",
      nativeQuery = true)
  Page<UnityLiteProjection> findAllUnified(@Param("term") String term, Pageable pageable);
}
