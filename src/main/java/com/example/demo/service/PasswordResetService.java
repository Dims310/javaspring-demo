package com.example.demo.service;

import java.util.UUID;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class PasswordResetService {
  private UserRepository userRepository;
  private PasswordResetTokenRepository tokenRepository;
  private EmailService emailService;
  private PasswordEncoder passwordEncoder;

  public PasswordResetService(
    UserRepository userRepository, 
    PasswordResetTokenRepository tokenRepository,
    EmailService emailService,
    PasswordEncoder passwordEncoder
  ) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.emailService = emailService;
    this.passwordEncoder = passwordEncoder;
  }

  public Boolean createPasswordResetToken(User user) {
    String token = UUID.randomUUID().toString();

    // tokenRepository.deleteByUser(user);

    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setUser(user);
    resetToken.setToken(token);
    resetToken.generateExpiredData();

    String resetUrl = "http://localhost:8080/user/forgotpassword/reset/" + token;

    tokenRepository.save(resetToken);

    Boolean result = emailService.sendMessage(
      user.getPerson().getEmail(),
      "Password Reset Request",
      resetUrl
    );

    if (!result) {
      return false;
    }
    return true;
  }

  public boolean resetPassword(String token, String newPassword) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token);

    if (resetToken == null) {
      throw new RuntimeException("Token has been expired");
    }

    if (resetToken.isExpired()) {
      tokenRepository.delete(resetToken);
      throw new RuntimeException("Token has expired");
    }

    User user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    tokenRepository.delete(resetToken);

    return true;
  }
}
