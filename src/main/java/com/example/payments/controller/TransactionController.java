package com.example.payments.controller;

import com.example.payments.exception.BadRequest;
import com.example.payments.model.request.TransactionRequest;
import com.example.payments.model.response.BalanceResponse;
import com.example.payments.model.response.TransactionResponse;
import com.example.payments.service.TransactionService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/deposit")
  public TransactionResponse deposit(@RequestBody TransactionRequest request) {
    validateTransactionRequest(request);
    return transactionService.deposit(request);
  }


  @PostMapping("/withdrawal")
  public TransactionResponse withdrawal(@RequestBody TransactionRequest request) {
    validateTransactionRequest(request);
    return transactionService.withdrawal(request);
  }

  @GetMapping("/balance")
  public BalanceResponse getBalance(
      @RequestParam(value = "userName") String userName,
      @RequestParam(value = "accountName") String accountName) {
    validateBalanceRequest(userName, accountName);
    return transactionService.getBalance(userName, accountName);
  }

  public void validateTransactionRequest(TransactionRequest request) {
    if (StringUtils.isEmpty(request.getUserName())) {
      throw new BadRequest("Invalid username Provided");
    }
    if (StringUtils.isEmpty(request.getAccountName())) {
      throw new BadRequest("Invalid accountName Provided");
    }
    if (request.getAmount() < 0) {
      throw new BadRequest("Invalid amount provided");
    }
  }

  public void validateBalanceRequest(String userName, String accountName) {
    if (StringUtils.isEmpty(userName)) {
      throw new BadRequest("Invalid username Provided");
    }
    if (StringUtils.isEmpty(accountName)) {
      throw new BadRequest("Invalid accountName Provided");
    }
  }
}
