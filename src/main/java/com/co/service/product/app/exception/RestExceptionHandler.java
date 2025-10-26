package com.co.service.product.app.exception;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String,Object>> onNotFound(Exception ex){
    log.warn("Recursos no encontrado {}", ex.toString());
    Map<String,Object> error = Map.of("status","404","title","Not Found","detail", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errors", List.of(error)));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> onGeneral(Exception ex){
    // Log full stacktrace for diagnosis
    log.error("Error no controlado", ex);
    Map<String,Object> err = Map.of("status","500","title","Server error","detail", ex.toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errors", List.of(err)));
  }
}