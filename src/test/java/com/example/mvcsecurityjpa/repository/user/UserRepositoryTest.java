package com.example.mvcsecurityjpa.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.repository.UserRepository;


/**
 * UserBoundaryTest
 */
@DataJpaTest
public class UserRepositoryTest {
  private final UserRepository userRepository;

  @Autowired
  UserRepositoryTest(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Test
  public void testValidUserSaving() {
    User user = new User("testuser", "test@example.com", "testuserpassword");
    // save the user
    assertThat(userRepository.save(user)).isInstanceOf(User.class);

    // test findByUsername method
    assertThat(userRepository.findByUsername(user.getUsername())).isInstanceOf(User.class);

    // test findByEmail method
    assertThat(userRepository.findByEmail(user.getEmail())).isInstanceOf(User.class);
  }

}