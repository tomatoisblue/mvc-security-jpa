package com.example.mvcsecurityjpa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.userDetails.CustomUserDetails;

/**
 * Authentication Helper
 */

@Component
class AuthenticationHelper {

  CustomUserDetails getAuthenticatedUserDetails() {
    return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  String redirectIfLoggedIn(String viewIfLoggedIn, String viewIfNotLoggedIn) {
    if (isLoggedIn()) {
      return viewIfLoggedIn;
    }
    return viewIfNotLoggedIn;
  }

  boolean isLoggedIn() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof UserDetails) {
      return true;
    }
    return false;
  }
}