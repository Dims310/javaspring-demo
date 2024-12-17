package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
  private String email;
  private String password;
  private String fullname;
  private String phone;
}
