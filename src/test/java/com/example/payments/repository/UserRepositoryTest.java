package com.example.payments.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.payments.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindByUserName() {
    User user = new User();
    user.setUserId(1L);
    user.setUserName("testUser");
    userRepository.save(user);
    User foundUser = userRepository.findByUserName("testUser");
    assertEquals(user, foundUser);
  }
}