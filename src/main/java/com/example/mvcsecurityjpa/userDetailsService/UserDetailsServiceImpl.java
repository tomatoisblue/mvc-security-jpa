package com.example.mvcsecurityjpa.userDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.repository.UserRepository;
import com.example.mvcsecurityjpa.userDetails.UserDetailsImpl;
/**
 * UserDetailsService
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepository;

  UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if(user == null) {
      throw new UsernameNotFoundException("Not Found");
    }
    return new UserDetailsImpl(user);
  }
}