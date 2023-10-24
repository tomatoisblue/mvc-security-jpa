package com.example.mvcsecurityjpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .formLogin(login -> login
          .loginPage("/login")
          .loginProcessingUrl("/login")
          .usernameParameter("email")
          .passwordParameter("password")
          .defaultSuccessUrl("/profile")
          .failureUrl("/login?error")
          .permitAll())
        .logout(logout -> logout
          .logoutUrl("/logout")
          .logoutSuccessUrl("/login"))
        .authorizeHttpRequests(auth -> auth
          .requestMatchers("/css/**", "/user/registration").permitAll()
          .anyRequest().authenticated());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}