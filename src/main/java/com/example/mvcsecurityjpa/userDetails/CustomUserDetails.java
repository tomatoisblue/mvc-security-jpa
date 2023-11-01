package com.example.mvcsecurityjpa.userDetails;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.mvcsecurityjpa.entity.User;

/**
 * CustomUserDetails
 */
public interface CustomUserDetails extends UserDetails {


  public Long getId();

  public User getUser();
}