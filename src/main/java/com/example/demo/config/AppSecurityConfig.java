package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeHttpRequests((auth) -> {
        try {
          auth
            .antMatchers("/").permitAll()
            // .antMatchers("/api/user/registration", "/api/user/auth").permitAll()
            .antMatchers("/user/registration", "/user/login", "/user/register", "/user/forgotpassword").permitAll()
            .antMatchers("/role/**").hasAuthority("Admin Toko")
            .antMatchers("/admin/**").hasAnyAuthority("Admin Toko")
            .antMatchers("/dashboard", "/user/edit/{id}").authenticated()
            .antMatchers("/api/user/{id}", "/api/user/edit/{id}").authenticated()
            .and()
            .formLogin()
            .loginPage("/user/login")
            .and()
            .httpBasic()
            .and()
            .logout()
            .logoutUrl("/user/logout")
            .logoutSuccessUrl("/user/login")
            .permitAll();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    return http.build();

    // http.csrf().disable()
    //   .cors()
    //   .and()
    //   .authorizeHttpRequests()
    //   .antMatchers("/api/auth/**").permitAll()
    //   .antMatchers("/api/admin/**").hasRole("Admin Toko")
    //   .and()
    //   .userDetailsService(uds)
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
