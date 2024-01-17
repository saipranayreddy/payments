package com.example.payments.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

  private String userName;
  private String firstName;
  private String lastName;

  private String accountName;
  private String emailId;

}
