package com.example.offers.demo.ui.model.response;

public enum ErrorMessages {
	MISSING_REQUIRED_FIELDS("Missing Required Fields. Please check documentation."), 
	RECORD_ALREADY_EXISTS("Record Already exists"), 
	INTERNAL_SERVER_ERROR("Inter Server Error"), 
	NO_RECORD_FOUND("No Record found with the given Id"),
	AUTHENTICATION_FAILED("Authentication Failed"),
	COULD_NOT_UPDATE("Could not Update Database"),
	COULD_NOT_DELETE("Could not Delete Record"),
	EMAIL_ADDRES_NOT_VERIFIED("Email Address could not be verified");

	private String errorMessage;

	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
