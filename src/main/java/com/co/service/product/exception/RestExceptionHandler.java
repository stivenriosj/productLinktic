package com.co.service.product.exception;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> onNotFound(Exception ex){
    Map<String,Object> error = Map.of("status","404","title","Not Found","detail", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errors", List.of(error)));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> onGeneral(Exception ex){
    Map<String,Object> err = Map.of("status","500","title","Server error","detail", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errors", List.of(err)));
  }
}