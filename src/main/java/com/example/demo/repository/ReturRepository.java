package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Retur;

@Repository
public interface ReturRepository extends JpaRepository<Retur, Integer>{
  
}
