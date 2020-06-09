package com.example.offers.demo.shared.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.offers.demo.DemoApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class UtilsTest {
	
	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGenerateUserId() {
		String userId1 = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		
		assertNotNull(userId1);
		assertTrue(userId1.length() == 30);
		assertTrue(!userId1.equalsIgnoreCase(userId2));
		System.out.println("user Id's generated are 1." + userId1 + " 2." + userId2);
		
		
	}
	
	@Test
	final void testHasTokenNotExpired() {
		String userId = utils.generateUserId(30);
		//String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNSktkM1pOZ2FGckxLRVpMNFcySUF0WGp0RWxjbXMiLCJleHAiOjE1ODk4OTE2ODd9.PyhawoYokmiyv3a28y-nIJTEqBXmQdLnkUhejpOi2ALzQrUK-oBhYXpch934fJcgMw8MFMMP8KHNs-zcjZIzmw";
		String token = utils.generatePasswordResetToken(userId);
		boolean hasTokenExpired = utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);
	}
	
	@Test
	final void testHasTokenExpired() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNSktkM1pOZ2FGckxLRVpMNFcySUF0WGp0RWxjbXMiLCJleHAiOjE1ODk4OTE2ODd9.PyhawoYokmiyv3a28y-nIJTEqBXmQdLnkUhejpOi2ALzQrUK-oBhYXpch934fJcgMw8MFMMP8KHNs-zcjZIzmw";
		
		boolean hasTokenExpired = utils.hasTokenExpired(token);
		assertTrue(hasTokenExpired);
	}

}
