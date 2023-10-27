package com.example.mvcsecurityjpa.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.repository.UserRepository;

/**
 * UserSearchService
 */
@Service
public class UserSearchService {

  private UserRepository userRepository;

  public UserSearchService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> searchUser(String query) {
    return userRepository.findByUsernameContainingIgnoreCase(query);
  }

}