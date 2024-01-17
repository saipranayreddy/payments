package com.example.payments.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.service.AccountService;
import com.example.payments.service.UserService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UserControllerTest {

  private UserController userController;

  private UserService userService;
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    accountService = mock(AccountService.class);
    userController = new UserController(userService, accountService);
  }

  @Test
  void testCreateAccountAndUser() {
    AccountRequest accountRequest = createAccountRequest();
    User user = createUser();
    Account createdAccount = createAccount(user);
    when(userService.createOrGetUser(accountRequest)).thenReturn(user);
    when(accountService.createAccount(user, accountRequest)).thenReturn(createdAccount);
    ResponseEntity<Map<String, Object>> responseEntity = userController.createAccountAndUser(
        accountRequest);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    Map<String, Object> responseBody = responseEntity.getBody();
    assertNotNull(responseBody);
    assertTrue(responseBody.containsKey("account"));
    assertEquals(createdAccount, responseBody.get("account"));
  }

  private AccountRequest createAccountRequest() {
    return new AccountRequest("John", "Doe", "johndoe", "SBI", "john@example.com");
  }

  private User createUser() {
    User user = new User();
    user.setUserName("johndoe");
    return user;
  }

  private Account createAccount(User user) {
    Account account = new Account();
    account.setUser(user);
    account.setAccountName("johndoe_account");
    account.setBalance(0.0);
    return account;
  }
}