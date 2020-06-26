package com.example.demo;

@Component
@Endpoint(id = "desc")
public class ApplicationHealth {
  
  @ReadOperation
  public String showDesc() {
    return "{\"desc\": \"My Application is Running just fine\"}";
  }
}