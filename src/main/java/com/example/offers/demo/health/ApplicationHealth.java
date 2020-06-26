package com.example.demo;

@Component
@Endpoint(id = "desc")
public class ApplicationHealth {
  
  @ReadOperation
  public string showDesc() {
    return "My Application is Running just fine";
  }
}