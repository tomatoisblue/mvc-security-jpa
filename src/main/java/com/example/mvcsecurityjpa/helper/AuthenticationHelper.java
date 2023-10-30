package com.example.mvcsecurityjpa.helper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.form.user.UserLoginForm;
import com.example.mvcsecurityjpa.userDetails.CustomUserDetails;

/**
 * Authentication Helper
 */

@Component
public class AuthenticationHelper {
  private AuthenticationManager authenticationManager;

  public AuthenticationHelper(AuthenticationManager authentiationManager) {
    this.authenticationManager = authentiationManager;
  }

  public Authentication authenticate(UserLoginForm form) {
    return authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));
  }

  public void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public Long getCurrentUserId() {
    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUser().getId();
  }

  public User getCurrentUser() {
    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUser();
  }

  public CustomUserDetails getAuthenticatedUserDetails() {
    return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public String redirectIfLoggedIn(String viewIfLoggedIn, String viewIfNotLoggedIn) {
    if (isLoggedIn()) {
      return viewIfLoggedIn;
    }
    return viewIfNotLoggedIn;
  }

  public boolean isLoggedIn() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof UserDetails) {
      return true;
    }
    return false;
  }

  public void logout(Long userId) {

  }


}