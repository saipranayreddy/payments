package com.example.payments.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.payments.exception.BadRequest;
import com.example.payments.model.request.TransactionRequest;
import com.example.payments.model.response.BalanceResponse;
import com.example.payments.model.response.TransactionResponse;
import com.example.payments.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionControllerTest {

  @Mock
  private TransactionService transactionService;

  @InjectMocks
  private TransactionController transactionController;

  @Test
  void testDeposit() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .userName("pranay")
        .amount(10)
        .build();
    TransactionResponse expectedResponse = TransactionResponse.builder()
        .message("Successfully deposited amount of 10")
        .data(10D).build();
    when(transactionService.deposit(request)).thenReturn(expectedResponse);
    TransactionResponse actualResponse = transactionController.deposit(request);
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void testDepositWithNegativeDeposit() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .userName("pranay")
        .amount(-10)
        .build();
    try {
      transactionController.deposit(request);
    } catch (Exception exception) {
      assertEquals("Invalid amount provided", exception.getMessage());
    }
  }

  @Test
  void testDepositWithNullAccountName() {
    TransactionRequest request = TransactionRequest.builder()
        .userName("pranay")
        .amount(10)
        .build();
    try {
      transactionController.deposit(request);
    } catch (Exception exception) {
      assertEquals("Invalid accountName Provided", exception.getMessage());
    }
  }

  @Test
  void testDepositWithNullUserName() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .amount(10)
        .build();
    try {
      transactionController.deposit(request);
    } catch (Exception exception) {
      assertEquals("Invalid username Provided", exception.getMessage());
    }
  }

  @Test
  void testWithdrawal() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .userName("pranay")
        .amount(10)
        .build();
    TransactionResponse expectedResponse = TransactionResponse.builder()
        .message("Successfully withdrawn amount of 10")
        .data(0D).build();
    when(transactionService.withdrawal(request)).thenReturn(expectedResponse);
    TransactionResponse actualResponse = transactionController.withdrawal(request);
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void testWithdrawalWithNegativeDeposit() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .userName("pranay")
        .amount(-10)
        .build();
    try {
      transactionController.withdrawal(request);
    } catch (Exception exception) {
      assertEquals("Invalid amount provided", exception.getMessage());
    }
  }

  @Test
  void testWithdrawalWithNullAccountName() {
    TransactionRequest request = TransactionRequest.builder()
        .userName("pranay")
        .amount(10)
        .build();
    try {
      transactionController.withdrawal(request);
    } catch (Exception exception) {
      assertEquals("Invalid accountName Provided", exception.getMessage());
    }
  }

  @Test
  void testWithdrawalWithNullUserName() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .amount(10)
        .build();
    try {
      transactionController.withdrawal(request);
    } catch (Exception exception) {
      assertEquals("Invalid username Provided", exception.getMessage());
    }
  }

  @Test
  void testBalanceRequest() {
    TransactionRequest request = TransactionRequest.builder()
        .accountName("SBI")
        .userName("pranay")
        .build();
    BalanceResponse expectedResponse = BalanceResponse.builder()
        .message("Successfully withdrawn amount of 10")
        .balance(10D).build();
    when(transactionService.getBalance(request.getUserName(), request.getAccountName())).thenReturn(
        expectedResponse);
    BalanceResponse actualResponse = transactionController.getBalance(request.getUserName(),
        request.getAccountName());
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void testEnquiryWithNullUserName() {
    try {
      transactionController.getBalance(null, "SBI");
    } catch (Exception exception) {
      assertEquals("Invalid username Provided", exception.getMessage());
    }
  }

  @Test
  void testEnquiryWithEmptyAccountName() {
    try {
      transactionController.getBalance("pranay", "");
    } catch (Exception exception) {
      assertEquals("Invalid accountName Provided", exception.getMessage());
    }
  }

}