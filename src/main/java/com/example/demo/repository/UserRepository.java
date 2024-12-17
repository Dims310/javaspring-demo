package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
  // TEST without PASSWORD
  // @Query("SELECT new com.example.demo.model.dto.UserDTO(u.id, p.fullname, p.nickname, r.name) "
  // + "FROM User u JOIN u.role r JOIN u.person p")
  // public List<UserDTO> getUser();

  // Untuk TEST
  @Query("SELECT new com.example.demo.model.dto.UserDTO(u.id, p.fullname, p.nickname, u.password, r.name) "
  + "FROM User u JOIN u.role r JOIN u.person p WHERE p.email = :email")
  public UserDTO getUserByEmail(@Param(value = "email") String email);
}
