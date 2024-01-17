package com.example.payments.controller;

import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.service.AccountService;
import com.example.payments.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;
  private final AccountService accountService;

  public UserController(UserService userService, AccountService accountService) {
    this.userService = userService;
    this.accountService = accountService;
  }

  @PostMapping("/createAccount")
  public ResponseEntity<Map<String, Object>> createAccountAndUser(
      @RequestBody AccountRequest request) {
    User user = userService.createOrGetUser(request);
    Account createdAccount = accountService.createAccount(user, request);
    Map<String, Object> response = new HashMap<>();
    response.put("account", createdAccount);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
