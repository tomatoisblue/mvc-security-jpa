package com.example.mvcsecurityjpa.service;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.repository.UserRepository;

/**
 * UserDeletionService
 */
@Service
public class UserDeletionService {
  private UserRepository userRepository;
  private UserServiceHelper userServiceHelper;

  public UserDeletionService(UserRepository userRepository, UserServiceHelper userServiceHelper) {
    this.userRepository = userRepository;
    this.userServiceHelper = userServiceHelper;
  }

  public void deleteUser(String username) {
    if (userServiceHelper.isUsernameExists(username)) {
      User user = userRepository.findByUsername(username);
      userRepository.delete(user);
    }
  }
}