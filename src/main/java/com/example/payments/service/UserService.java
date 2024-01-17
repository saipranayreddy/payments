package com.example.payments.service;

import com.example.payments.exception.InvalidUserDataException;
import com.example.payments.model.entity.User;
import com.example.payments.model.request.AccountRequest;
import com.example.payments.repository.UserRepository;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);


  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createOrGetUser(AccountRequest request) {
    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .userName(request.getUserName())
        .emailId(request.getEmailId())
        .build();
    User userDetails = userRepository.findByUserName(request.getUserName());
    logger.info("Successfully Fetched user Details: {}", request);
    if (Objects.isNull(userDetails)) {
      return userRepository.save(user);
    } else {
      return userDetails;
    }
  }

  public User getUser(String username) {
    User user = userRepository.findByUserName(username);
    if (Objects.isNull(user)) {
      throw new InvalidUserDataException("Register this user");
    }
    logger.info("Successfully Fetched user Details: {}", username);
    return user;
  }
}
