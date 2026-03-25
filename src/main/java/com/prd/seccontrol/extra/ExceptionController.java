package com.prd.seccontrol.extra;

import com.prd.seccontrol.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(value = {Exception.class, RuntimeException.class})
  public ResponseEntity<ErrorResponse> defaultErrorHandler(Exception e) {
    e.printStackTrace();

    return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
  }
}
