package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
public class DashboardController {
  private UserRepository userRepository;

  public DashboardController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    if (principal instanceof User) {
      User loggedUser = (User) principal;
      com.example.demo.model.User user = userRepository.findById(Integer.parseInt(loggedUser.getUsername())).get();

      model.addAttribute("user", user);

      String role = loggedUser.getAuthorities()
                                .stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElse("ROLE_USER");
      model.addAttribute("role", role);
    }

    return "dashboard";
  }
}
