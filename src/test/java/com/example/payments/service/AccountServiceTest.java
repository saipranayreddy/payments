package com.example.payments.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountServiceTest {

  private AccountService accountService;

  private AccountRepository accountRepository;

  @BeforeEach
  void setUp() {
    accountRepository = mock(AccountRepository.class);
    accountService = new AccountService(accountRepository);
  }

  @Test
  void testCreateAccount() {
    User user = new User();
    user.setUserName("username");
    AccountRequest accountRequest = AccountRequest.builder().accountName("accountName").build();
    when(accountRepository.findByUserAndAccountName(user, "accountName")).thenReturn(null);
    when(accountRepository.save(any(Account.class))).thenAnswer(invocation->{
      Account account = invocation.getArgument(0);
      return account;
    });
    Account createdAccount = accountService.createAccount(user, accountRequest);
    assertEquals("accountName", createdAccount.getAccountName());
    assertEquals(0.0, createdAccount.getBalance());
    assertEquals(user, createdAccount.getUser());
  }

  @Test
  void testCreateAccountWithExistingAccount() {
    // Mocking
    User user = new User();
    user.setUserName("username");
    AccountRequest accountRequest = AccountRequest.builder().accountName("accountName").build();
    Account existingAccount = new Account();
    when(accountRepository.findByUserAndAccountName(user, "accountName")).thenReturn(
        existingAccount);
    assertThrows(
        InvalidUserDataException.class, () -> accountService.createAccount(user, accountRequest));
  }

  @Test
  void testGetAccount() {
    User user = new User();
    user.setUserName("username");
    Account expectedAccount = new Account();
    expectedAccount.setUser(user);
    expectedAccount.setAccountName("accountName");
    when(accountRepository.findByUserAndAccountName(user, "accountName")).thenReturn(
        expectedAccount);
    Account retrievedAccount = accountService.getAccount(user, "accountName");
    assertEquals(expectedAccount, retrievedAccount);
  }

  @Test
  void testGetAccountNonExistingAccount() {
    User user = new User();
    user.setUserName("username");
    when(accountRepository.findByUserAndAccountName(user, "nonExistingAccount")).thenReturn(null);
    assertThrows(InvalidUserDataException.class,
        () -> accountService.getAccount(user, "nonExistingAccount"));
  }

  @Test
  void testGetAccountInvalidUser() {
    User user = new User();
    user.setUserName("username");
    Account invalidAccount = new Account();
    invalidAccount.setUser(new User());
    when(accountRepository.findByUserAndAccountName(user, "invalidAccount")).thenReturn(
        invalidAccount);
    assertThrows(InvalidUserDataException.class,
        () -> accountService.getAccount(user, "invalidAccount"));
  }

  @Test
  void testSaveAccount() {
    Account account = new Account();
    when(accountRepository.save(account)).thenReturn(account);
    Account savedAccount = accountService.saveAccount(account);
    assertEquals(account, savedAccount);
  }
}