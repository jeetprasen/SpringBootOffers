package com.example.offers.demo.shared.dto;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.offers.demo.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Utils {
	private final Random RANDOM = new SecureRandom();
	private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstubwxyz";
	private final String ALPHABET_ONLY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstubwxyz";
	//	private final int ITERATIONS = 10000;
	//	private final int KEY_LENGTH = 256;

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	public String generateAddressId(int length) {
		return generateRandomString(length);
	}
	
	public String generateStringOnly(int length) {
		return generateRandomStringOnly(length);
	}
	
	public boolean generateRandomBoolean() {
		return generateRandomBooleanVar(); 
	}

	private boolean generateRandomBooleanVar() {
		return new Random().nextBoolean();
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}
	
	private String generateRandomStringOnly(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET_ONLY.charAt(RANDOM.nextInt(ALPHABET_ONLY.length())));
		}

		return new String(returnValue);
	}

	public static boolean hasTokenExpired(String token) {

		boolean hasExpired = true;
		try {
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody();

			Date tokenExpiration = claims.getExpiration();
			Date today = new Date();
			hasExpired = tokenExpiration.before(today);
		} catch (ExpiredJwtException ex) {
			hasExpired = true;
		}

		return hasExpired;
	}

	public String generatePasswordResetToken(String userId) {

		String token = Jwts.builder().setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret()).compact();

		return token;
	}

}
