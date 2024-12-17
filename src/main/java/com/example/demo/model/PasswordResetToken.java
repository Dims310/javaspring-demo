package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_password_reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String token;

  @OneToOne
  @JoinColumn(name = "tb_users_id", referencedColumnName = "id")
  private User user;

  @Column(name = "expired_at")
  private LocalDateTime expiredAt;

  public void generateExpiredData() {
    expiredAt = LocalDateTime.now().plusMinutes(30);
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiredAt);
  }
}
