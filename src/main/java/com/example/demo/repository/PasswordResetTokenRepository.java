package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer>{
  PasswordResetToken findByToken(String token);

  @Query(value = "SELECT tb_users_id FROM tb_password_reset_token WHERE token = :token", nativeQuery = true)
  public Integer findIdUserByToken(@Param("token") String token);

  @Query(value = "SELECT * FROM tb_password_reset_token WHERE tb_users_id = :userId", nativeQuery = true)
  public PasswordResetToken findByUserId(@Param("userId") Integer userId);
}
