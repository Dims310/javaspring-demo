package com.example.demo.utils;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.repository.UserRepository;

@Component
public class MyUserDetailsService implements UserDetailsService {
 
  private UserRepository userRepository;

  public MyUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserDTO userDTO = userRepository.getUserByEmail(email);
    User user = userRepository.findById(userDTO.getId()).get();

    return new org.springframework.security.core.userdetails.User(
      email,
      user.getPassword(),
      Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRoleName()))
    );
    
  }
}
