package com.example.payments.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

  private UserService userService;

  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  void testCreateOrGetUserCreateNewUser() {
    AccountRequest accountRequest = createAccountRequest();
    when(userRepository.findByUserName(accountRequest.getUserName())).thenReturn(null);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    User createdOrExistingUser = userService.createOrGetUser(accountRequest);
    assertEquals(accountRequest.getUserName(), createdOrExistingUser.getUserName());
  }

  @Test
  void testCreateOrGetUserExistingUser() {
    AccountRequest accountRequest = createAccountRequest();
    User existingUser = new User();
    existingUser.setUserName(accountRequest.getUserName());
    when(userRepository.findByUserName(accountRequest.getUserName())).thenReturn(existingUser);
    User createdOrExistingUser = userService.createOrGetUser(accountRequest);
    assertEquals(accountRequest.getUserName(), createdOrExistingUser.getUserName());
  }

  @Test
  void testGetUser() {
    String username = "existingUser";
    User existingUser = new User();
    existingUser.setUserName(username);
    when(userRepository.findByUserName(username)).thenReturn(existingUser);
    User retrievedUser = userService.getUser(username);
    assertEquals(existingUser, retrievedUser);
  }

  @Test
  void testGetUserNonExistingUser() {
    String nonExistingUsername = "nonExistingUser";
    when(userRepository.findByUserName(nonExistingUsername)).thenReturn(null);
    assertThrows(InvalidUserDataException.class, () -> userService.getUser(nonExistingUsername));
  }

  private AccountRequest createAccountRequest() {
    return new AccountRequest("John", "Doe", "johndoe", "sbi", "john@example.com");
  }
}