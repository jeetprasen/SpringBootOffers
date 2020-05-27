package com.example.offers.demo.security;

import com.example.offers.demo.context.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_DATE = 864000000; 	//10 days - in millisecond
	public static final long EXPIRATION_TIME = 60000;		//5 min - 300000 in millisecond; 1min - 60000 millisecond
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";

    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "/users/password-reset";
    public static final String H2_CONSOLE = "/h2-console/**";
    
	//public static final String TOKEN_SECRET = "jdfg6t78ut";
	
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
