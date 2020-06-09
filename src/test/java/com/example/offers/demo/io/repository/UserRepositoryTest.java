package com.example.offers.demo.io.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.offers.demo.DemoApplication;
import com.example.offers.demo.io.entity.UserEntity;
import com.example.offers.demo.io.repositories.UserRepository;
import com.example.offers.demo.shared.dto.Utils;
import com.example.offers.demo.test.utils.TestUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	String firstName="Prasen";
	String lastName="Jeet";
	String userId="DWEJ6ZifKK";
	
	static boolean recordsCreated = false;
	
	int count = 4;
	
	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated)createRecord();
	}
	
	private void createRecord() {
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		
		TestUtils testUtils = new TestUtils(utils, bCryptPasswordEncoder);
		List<UserEntity> generatedUsers = testUtils.generateRandomUsers(count);
		
		generatedUsers.get(0).setFirstName(firstName);
		generatedUsers.get(1).setFirstName(firstName);
		generatedUsers.get(0).setLastName(lastName);
		generatedUsers.get(1).setLastName(lastName);
		generatedUsers.get(0).setUserId(userId);
		//userId = generatedUsers.get(0).getUserId();
		
		userRepository.saveAll(generatedUsers);		
		stopwatch.stop();
		System.out.println("Time taken for generating and saving " + count + " user(s): " + stopwatch.getTime(TimeUnit.MILLISECONDS));	
		recordsCreated = true;
	}

	@Test
	final void testGetVerifiedUsers() {
		Pageable pagableRequest = PageRequest.of(0, 2);
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pagableRequest);
		
		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() > 0);
	}
	
	@Test
	final void testFindByfirstName() {
		List<UserEntity> users = userRepository.findUsersByFirstName(firstName);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		assertTrue(users.get(0).getFirstName().equals(firstName));
	}
	
	@Test
	final void testFindByLastName() {
		List<UserEntity> users = userRepository.findUsersByLastName(lastName);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		assertTrue(users.get(0).getLastName().equals(lastName));
	}
	
	@Test
	final void testFindByKeyword() {
		List<UserEntity> users = userRepository.findUsersByKeyword("rase");
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		assertTrue(users.get(0).getLastName().contains("ee"));
	}
	
	@Test
	final void findUsersFirstAndLastNameOnlyByKeyword() {
		List<Object[]> users = userRepository.findUsersFirstAndLastNameByKeyword("rase");
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		assertTrue(((String) (users.get(0))[1]).contains("ee"));
		assertTrue(((String) (users.get(1))[0]).contains("Prasen"));
		
		System.out.println("First Name is: " + String.valueOf( users.get(0)[0] ) );
		System.out.println("Last Name is:  " + String.valueOf( users.get(0)[1] ) );
		
	} 
	
	@Test
	final void updateUserEmailVerificationStatus() {
		boolean newEmailVerificationStatus = false;
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, userId);
		UserEntity user = userRepository.findByUserId(userId);
		assertTrue(user.isEmailVerificationStatus() == newEmailVerificationStatus);
		
		newEmailVerificationStatus = true;
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, userId);
		user = userRepository.findByUserId(userId);
		assertTrue(user.isEmailVerificationStatus() == newEmailVerificationStatus);
		
	}
	
	// Testing JPQL
	
	@Test
	final void findUserEntityByUserId() {
		UserEntity user = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(user);
		assertTrue(user.getUserId().equals(userId));
	}
	
	@Test
	final void findUserFullNameByUserId() {
		List<Object[]> user = userRepository.findUserFullNameByUserId(userId);
		
		assertNotNull(user);
		assertTrue(user.size() == 1);
		assertTrue(user.get(0)[0].equals(firstName));
	}
	

	@Test
	final void updateUserEntityEmailVerificationStatus() {
		boolean newEmailVerificationStatus = false;
		userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, userId);
		UserEntity user = userRepository.findByUserId(userId);
		assertTrue(user.isEmailVerificationStatus() == newEmailVerificationStatus);
		
		newEmailVerificationStatus = true;
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, userId);
		user = userRepository.findByUserId(userId);
		assertTrue(user.isEmailVerificationStatus() == newEmailVerificationStatus);
		
	}
}
