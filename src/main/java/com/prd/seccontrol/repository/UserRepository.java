package com.prd.seccontrol.repository;

import com.prd.seccontrol.model.entity.User;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByUsername(String username);

  @Query(
      "UPDATE User u SET u.lastLogin = :date WHERE u.username = :username"
  )

  @Modifying
  void updateLastLoginTimeByUsername(String username, Date date);
}
