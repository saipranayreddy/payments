package com.example.payments.exception;


import com.example.payments.model.response.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


  @ExceptionHandler(value = {ContentNotFoundException.class})
  public ResponseEntity<Object> handleContentNotFoundException(ContentNotFoundException ex,
      WebRequest request) {
    String errorMessage = ex.getMessage();
    logger.error("Failed to process request due to :{}, ex: {}, request: {}", errorMessage, ex, request);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(Response.builder()
            .success(false)
            .message(errorMessage)
            .statusCode(HttpStatus.NO_CONTENT.value())
            .build());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    String errorMessage = ex.getMessage();
    logger.error("Failed to process request due to :{}, ex: {}, request: {}", errorMessage, ex, request);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Response.builder()
            .success(false)
            .message(errorMessage)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .build());
  }

  @ExceptionHandler(value = {BadRequest.class})
  public ResponseEntity<Object> handleBadRequestException(BadRequest ex, WebRequest request) {
    String errorMessage = ex.getMessage();
    logger.error("Failed to process request due to :{}, ex: {}, request: {}", errorMessage, ex, request);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Response.builder()
            .success(false)
            .message(errorMessage)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .build());
  }


  @ExceptionHandler(value = {InvalidUserDataException.class})
  public ResponseEntity<Object> handleInvalidUserDataException(InvalidUserDataException ex,
      WebRequest request) {
    String errorMessage = ex.getMessage();
    logger.error("Failed to process request due to :{}, ex: {}, request: {}", errorMessage, ex, request);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Response.builder()
            .success(false)
            .message(errorMessage)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .build());
  }


}

