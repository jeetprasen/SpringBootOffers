package com.example.offers.demo.exception;

public class UserServiceException extends RuntimeException{

	private static final long serialVersionUID = 2308740907130150810L;
	
	public UserServiceException(String message) {
		super(message);
	}
}
