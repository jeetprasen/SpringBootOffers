package com.example.offers.demo.health;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestParam;

@Component
@Endpoint(id = "desc")
public class ApplicationHealth {
  
  @ReadOperation
  public String showDesc() {
    return "{\"desc\": \"My Application is Running just fine\"}";
  }
}