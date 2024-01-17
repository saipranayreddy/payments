package com.example.payments.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @SequenceGenerator(name = "account_sequence",
  sequenceName = "account_sequence",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long accountId;
  private Double balance;
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "userId")
  private User user;

  @NonNull
  private String accountName;

}
