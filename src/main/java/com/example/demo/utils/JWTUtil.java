package com.example.demo.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {
  @Value("{jwt_secret}")
  private String secret;

  // Membuat token JWT dengan berbasis pada email
  public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
    return JWT.create()
      .withSubject("User Details")
      .withClaim("email", email)
      .withIssuedAt(new Date())
      .withIssuer("toserba_app")
      .sign(Algorithm.HMAC256(secret)); // enkripsi
  }

  // Validasi token JWT dan men-decode JWT menjadi email
  public String validateToken(String token) throws JWTVerificationException {
    JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
      .withSubject("User Details")
      .withIssuer("toserba_app")
      .build();
    DecodedJWT jwt = verifier.verify(token);
    return jwt.getClaim("email").asString();
  }
}
