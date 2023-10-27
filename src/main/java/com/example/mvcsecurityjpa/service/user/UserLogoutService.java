package com.example.mvcsecurityjpa.service.user;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.repository.UserRepository;

/**
 * UserDeletionService
 */
@Service
public class UserLogoutService {
  private UserRepository userRepository;

  public UserLogoutService(UserRepository userRepository, UserServiceHelper userServiceHelper) {
    this.userRepository = userRepository;
  }

  public void logout(Long userId) throws Exception {
    try {
      userRepository.deleteById(userId);
    } catch (Exception e) {
      throw e;
    }
  }
}