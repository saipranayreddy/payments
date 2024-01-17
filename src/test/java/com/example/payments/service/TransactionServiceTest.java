package com.example.payments.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.TransactionRequest;
import com.example.payments.model.response.BalanceResponse;
import com.example.payments.model.response.TransactionResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionServiceTest {

  private TransactionService transactionService;

  private UserService userService;

  private AccountService accountService;

  @BeforeEach
  void setUp() {
    userService = mock(UserService.class);
    accountService = mock(AccountService.class);
    transactionService = new TransactionService(userService, accountService);
  }

  @Test
  void testDeposit() {
    TransactionRequest request = new TransactionRequest("username", "accountName", 100.0);
    User user = new User();
    user.setUserName("username");
    when(userService.getUser("username")).thenReturn(user);
    Account account = new Account();
    account.setBalance(200.0);
    when(accountService.getAccount(user, "accountName")).thenReturn(account);
    when(accountService.saveAccount(account)).thenReturn(account);
    TransactionResponse response = transactionService.deposit(request);
    assertEquals("Deposit successful", response.getMessage());
    assertEquals(300.0, response.getData());
  }

  @Test
  void testWithdrawal() {
    TransactionRequest request = new TransactionRequest("username", "accountName", 50.0);
    User user = new User();
    user.setUserName("username");
    when(userService.getUser("username")).thenReturn(user);
    Account account = new Account();
    account.setBalance(100.0);
    when(accountService.getAccount(user, "accountName")).thenReturn(account);
    when(accountService.saveAccount(account)).thenReturn(account);
    TransactionResponse response = transactionService.withdrawal(request);
    assertEquals("Withdrawal successful", response.getMessage());
    assertEquals(50.0, response.getData());
  }

  @Test
  void testWithdrawalWithInsufficientFunds() {
    TransactionRequest request = new TransactionRequest("username", "accountName", 150.0);
    User user = new User();
    user.setUserName("username");
    when(userService.getUser("username")).thenReturn(user);
    Account account = new Account();
    account.setBalance(100.0);
    when(accountService.getAccount(user, "accountName")).thenReturn(account);
    assertThrows(InvalidUserDataException.class, () -> transactionService.withdrawal(request));
  }

  @Test
  void testGetBalance() {
    String username = "username";
    String accountName = "accountName";
    User user = new User();
    user.setUserName(username);
    when(userService.getUser(username)).thenReturn(user);
    Account account = new Account();
    account.setBalance(100.0);
    when(accountService.getAccount(user, accountName)).thenReturn(account);
    BalanceResponse response = transactionService.getBalance(username, accountName);
    assertEquals("Balance inquiry successful", response.getMessage());
    assertEquals(100.0, response.getBalance());
  }

  @Test
  void testConcurrentDeposits() throws InterruptedException, ExecutionException {
    User user = new User();
    user.setUserName("username");
    when(userService.getUser("username")).thenReturn(user);
    Account account = new Account();
    account.setAccountName("accountName");
    account.setUser(user);
    account.setBalance(0.0);
    when(accountService.getAccount(user, "accountName")).thenReturn(account);
    when(accountService.saveAccount(any(Account.class))).thenAnswer(invocation -> {
      Account savedAccount = invocation.getArgument(0);
      Thread.sleep(100);
      return savedAccount;
    });
    int numThreads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
    List<Callable<TransactionResponse>> depositTasks = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      TransactionRequest request = new TransactionRequest("username", "accountName", 100.0);
      depositTasks.add(() -> transactionService.deposit(request));
    }
    List<Future<TransactionResponse>> futures = executorService.invokeAll(depositTasks);
    for (Future<TransactionResponse> future : futures) {
      TransactionResponse response = future.get();
      assertEquals("Deposit successful", response.getMessage());
    }
    assertEquals(numThreads * 100.0, account.getBalance());
    executorService.shutdown();
  }

  @Test
  void testConcurrentTransactions() throws InterruptedException {
    User user = new User();
    user.setUserName("username");
    when(userService.getUser("username")).thenReturn(user);
    Account account = new Account();
    account.setBalance(500.0);
    when(accountService.getAccount(user, "accountName")).thenReturn(account);
    when(accountService.saveAccount(any(Account.class))).thenAnswer(invocation -> {
      Account savedAccount = invocation.getArgument(0);
      Thread.sleep(100);
      return savedAccount;
    });
    int numThreads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
    List<Callable<Object>> tasks = new ArrayList<>();

    for (int i = 0; i < numThreads; i++) {
      int transactionType = i % 2; // 0: Deposit, 1: Withdrawal
      TransactionRequest request = createTransactionRequest(transactionType);
      tasks.add(() -> performTransaction(request, transactionType));
    }
    executorService.invokeAll(tasks);
    assertEquals(750.0, account.getBalance());
    executorService.shutdown();
  }

  private TransactionRequest createTransactionRequest(int transactionType) {
    switch (transactionType) {
      case 0:
        return new TransactionRequest("username", "accountName", 100.0);
      case 1:
        return new TransactionRequest("username", "accountName", 50.0);
      default:
        throw new IllegalArgumentException("Invalid transaction type");
    }
  }

  private Object performTransaction(TransactionRequest request, int transactionType) {
    switch (transactionType) {
      case 0:
        return transactionService.deposit(request);
      case 1:
        return transactionService.withdrawal(request);
      default:
        throw new IllegalArgumentException("Invalid transaction type");
    }
  }

}