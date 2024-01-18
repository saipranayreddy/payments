package com.example.payments.service;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.TransactionRequest;
import com.example.payments.model.response.BalanceResponse;
import com.example.payments.model.response.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

  private final UserService userService;
  private final AccountService accountService;
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


  public TransactionService(UserService userService, AccountService accountService) {
    this.userService = userService;
    this.accountService = accountService;
  }


  public TransactionResponse deposit(TransactionRequest request) {
    User user = userService.getUser(request.getUserName());
    return getTransactionResponseAfterDeposit(request, user);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 30)
  public TransactionResponse getTransactionResponseAfterDeposit(TransactionRequest request,
      User user) {
    try {
      logger.info("Fetching account balance for deposit: {}", request);
      Account account = accountService.getAccount(user, request.getAccountName());
      account.setBalance(account.getBalance() + request.getAmount());
      logger.info("Initiating deposit: {}", request);
      Account savedAccount = accountService.saveAccount(account);
      logger.info("Deposit Transaction successfully processed: {}", savedAccount);
      return new TransactionResponse("Deposit successful", account.getBalance());
    } catch (Exception exception) {
      logger.error("Error processing deposit transaction: {}", request, exception);
      throw exception;
    }
  }

  public TransactionResponse withdrawal(TransactionRequest request) {
    User user = userService.getUser(request.getUserName());
    return getTransactionResponseAfterWithdrawal(request, user);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 30)
  public TransactionResponse getTransactionResponseAfterWithdrawal(TransactionRequest request,
      User user) {
    try {
      logger.info("Fetching account balance for withdrawal: {}", request);
      Account account = accountService.getAccount(user, request.getAccountName());
      if (account.getBalance() < request.getAmount()) {
        throw new InvalidUserDataException("Insufficient funds in the user account");
      }
      account.setBalance(account.getBalance() - request.getAmount());
      logger.info("Initiating withdrawal: {}", request);
      Account savedAccount = accountService.saveAccount(account);
      logger.info("Withdrawal Transaction successfully processed: {}", savedAccount);
      return new TransactionResponse("Withdrawal successful", account.getBalance());
    } catch (Exception exception) {
      logger.error("Error processing withdrawal transaction: {}", request, exception);
      throw exception;
    }
  }

  public BalanceResponse getBalance(String username, String accountName) {
    User user = userService.getUser(username);
    return getBalanceResponse(accountName, user);
  }

  private BalanceResponse getBalanceResponse(String accountName, User user) {
    try {
      logger.info("Fetching account balance for balance Enquiry: {}, {}", user, accountName);
      Account account = accountService.getAccount(user, accountName);
      logger.info("Balance successfully fetched: {}, {}", user, accountName);
      return new BalanceResponse("Balance inquiry successful", account.getBalance());
    } catch (Exception exception) {
      logger.error("Error fetching balance: {}, {}", user, accountName);
      throw exception;
    }
  }

}
