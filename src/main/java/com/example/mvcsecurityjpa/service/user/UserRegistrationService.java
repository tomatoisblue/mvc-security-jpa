package com.example.mvcsecurityjpa.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.repository.UserRepository;


/**
 * UserRegistrationService
 */
@Service
public class UserRegistrationService {

  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;

  public UserRegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public void userRegistration(String username, String email, String password) {

    // Generate a hashed password by BCrypt
    String hashedPassword = passwordEncoder.encode(password);
    // Create a User object with the hashed password
    User user = new User(username, email, hashedPassword);
    userRepository.saveAndFlush(user);
  }


  public boolean isUsernameExists(String username) {
    if (userRepository.findByUsername(username) != null) {
      return true;
    }
    return false;
  }


}