package com.example.offers.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.offers.demo.exception.UserServiceException;
import com.example.offers.demo.io.entity.AddressEntity;
import com.example.offers.demo.io.entity.UserEntity;
import com.example.offers.demo.io.repositories.UserRepository;
import com.example.offers.demo.shared.dto.AddressDTO;
import com.example.offers.demo.shared.dto.UserDTO;
import com.example.offers.demo.shared.dto.Utils;

class UserServiceImplTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserServiceImpl userService; 
		
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	Utils utils;
	
	@Autowired
	//AmazonSES amazonSES;
	
	String userId = "123_asdflkjoOLL";
	String password = "XLKJlasdjpojlasdf";
	UserEntity userEntity;
	AddressDTO addressDTO;
	List<AddressDTO> addressList;
	UserDTO userDTO;
	String addressID = "olasudkflllasdkjf";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userEntity = new UserEntity();
		userEntity.setId(1234);
		userEntity.setFirstName("test");
		userEntity.setLastName("last");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(password);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("xyzzzz");
		userEntity.setAddresses(getAddressEntity());
		
		
		userDTO = new UserDTO();
		userDTO.setFirstName("Temp");
		userDTO.setLastName("Last");
		userDTO.setPassword("asjouialsdfljka");
		userDTO.setEmail("testmail@mail.com");
		userDTO.setAddresses(getAddressList());
	}

	private List<AddressEntity> getAddressEntity() {
		List<AddressDTO> addresses = getAddressList();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
				
		return new ModelMapper().map(addresses, listType);
	}

	@Test
	final void testGetUser() {
		
		when(userRepository.findUserByEmail(anyString()) ).thenReturn(userEntity);
		
		UserDTO userDTO = userService.getUser("");
		assertNotNull(userDTO);
		assertEquals("test", userDTO.getFirstName());
	}
	
	@Test
	final void testGetUser_UserNameNotFoundException() {
		when(userRepository.findUserByEmail(anyString()) ).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, 
				()-> { userService.getUser("test@test.com"); }
		);
	}
	
	@Test
	final void testCreateUser_UserServiceException() {
		when(userRepository.findUserByEmail(anyString()) ).thenReturn(userEntity);
		assertThrows(UserServiceException.class, 
				()-> { userService.createUser(userDTO); }
		);
	}
	
	@Test
	final void testCreateUserMethod() {
		
		
		when(userRepository.findUserByEmail(anyString()) ).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn(addressID);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(password);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		//Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDTO.class));
		
		
		
		UserDTO storedUserDetails = userService.createUser(userDTO);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(storedUserDetails.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		assertEquals(storedUserDetails.getAddresses().size(), 2);
	}
	
	private List<AddressDTO> getAddressList() {
		
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setType("Shipping");
		addressDTO.setCountry("INDIA");
		addressDTO.setCity("Delhi");
		addressDTO.setPostalCode("101081");
		addressDTO.setStreetName("AnandpurDham");
		
		AddressDTO billingAddressDTO = new AddressDTO();
		addressDTO.setType("billing");
		addressDTO.setCountry("INDIA");
		addressDTO.setCity("Delhi");
		addressDTO.setPostalCode("101081");
		addressDTO.setStreetName("AnandpurDham");
		
		
		addressList = new ArrayList<>();
		addressList.add(addressDTO);
		addressList.add(billingAddressDTO);
		
		return addressList;
	}

}
