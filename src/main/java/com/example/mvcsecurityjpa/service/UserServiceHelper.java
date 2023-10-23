package com.example.mvcsecurityjpa.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.repository.UserRepository;


/**
 * UserServiceHelper
 */
@Service
public class UserServiceHelper {

  private UserRepository userRepository;

  public UserServiceHelper(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean isUsernameExists(String username) {
    if (userRepository.findByUsername(username) != null) {
      return true;
    }
    return false;
  }


}