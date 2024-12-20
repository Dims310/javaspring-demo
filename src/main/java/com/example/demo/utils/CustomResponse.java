package com.example.demo.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse {
  
  public static ResponseEntity<Object> generate(HttpStatus httpStatus, String message, Object object){
    Map<String, Object> response = new HashMap<>();
    response.put("status", httpStatus);
    response.put("message", message);
    response.put("data", object);
    return new ResponseEntity<Object>(response, httpStatus);
  }

  public static ResponseEntity<Object> generate(HttpStatus httpStatus, String message) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", httpStatus);
    response.put("message", message);
    return new ResponseEntity<Object>(response, httpStatus);
  }
}
