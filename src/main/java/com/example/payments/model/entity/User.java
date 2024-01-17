package com.example.payments.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

  @Id
  @SequenceGenerator(name = "user_sequence",
      sequenceName = "user_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long userId;
  @Column(unique = true)
  @NonNull
  private String userName;
  private String firstName;
  private String lastName;
  @Column(unique = true)
  private String emailId;

}
