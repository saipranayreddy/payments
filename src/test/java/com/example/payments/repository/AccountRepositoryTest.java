package com.example.payments.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

  @Autowired private AccountRepository accountRepository;

  @Test
  public void testFindByUserAndAccountName() {
    User user = new User();
    user.setUserId(1L);
    user.setUserName("testUser");
    Account account = new Account();
    account.setAccountId(1L);
    account.setAccountName("testAccount");
    account.setUser(user);
    accountRepository.save(account);
    Account foundAccount = accountRepository.findByUserAndAccountName(user, "testAccount");
    assertEquals(account, foundAccount);
  }
}