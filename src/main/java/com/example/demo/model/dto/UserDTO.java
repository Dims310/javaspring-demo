package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private Integer id;
  private String fullname;
  private String nickname;
  private String password;
  private String roleName;
  
  @Override
  public String toString() {
    return "UserDTO [id=" + id + ", fullname=" + fullname + ", nickname=" + nickname + ", password=" + password
        + ", roleName=" + roleName + ", getNickname()=" + getNickname() + ", getPassword()=" + getPassword() + "]";
  }
  
}
