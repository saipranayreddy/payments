package com.example.payments.service;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.repository.AccountRepository;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);


  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(User user, AccountRequest request) {

    Account userAccount = accountRepository.findByUserAndAccountName(user,
        request.getAccountName());
    logger.info("Successfully Fetched Account Details: {}, {}", user, request);
    if (!Objects.isNull(userAccount)) {
      throw new InvalidUserDataException("Account already exists for this user and account");
    }
    Account account = Account.builder()
        .user(user)
        .balance(0.0)
        .accountName(request.getAccountName()).build();
    return accountRepository.save(account);
  }

  public Account getAccount(User user, String accountName) {
    Account account = accountRepository.findByUserAndAccountName(user, accountName);
    logger.info("Successfully Fetched user Details: {}, {}", user, accountName);
    if (Objects.isNull(account)) {
      throw new InvalidUserDataException("Account does not exist for this user ");
    } else if (!account.getUser().equals(user)) {
      throw new InvalidUserDataException("Invalid accountid of the user");
    }
    return account;
  }

  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }
}
