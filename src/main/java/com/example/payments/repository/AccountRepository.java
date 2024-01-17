package com.example.payments.repository;

import com.example.payments.model.entity.Account;
import com.example.payments.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account findByUserAndAccountName(User user, String accountName);
}
