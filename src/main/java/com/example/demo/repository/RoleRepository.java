package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  @Query(value = "SELECT * FROM tb_m_roles WHERE level = (SELECT MAX(level) FROM tb_m_roles)", nativeQuery = true)
  public Role lowLevelRole();
  
}
