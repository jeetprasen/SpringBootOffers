package com.example.offers.demo.ui.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.offers.demo.io.entity.UserEntity;
import com.example.offers.demo.service.impl.UserServiceImpl;
import com.example.offers.demo.shared.dto.AddressDTO;
import com.example.offers.demo.shared.dto.UserDTO;
import com.example.offers.demo.ui.model.response.UserRest;

class UserControllerTest {
	
	@InjectMocks
	UserController userController;
	
	@Mock
	UserServiceImpl userService;
	
	UserDTO userDTO;
	
	String userId = "123_asdflkjoOLL";
	String password = "XLKJlasdjpojlasdf";
	UserEntity userEntity;
	AddressDTO addressDTO;
	List<AddressDTO> addressList;
	String addressID = "olasudkflllasdkjf";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userDTO = new UserDTO();
		userDTO.setFirstName("Temp");
		userDTO.setLastName("Last");
		userDTO.setPassword("asjouialsdfljka");
		userDTO.setEmail("testmail@mail.com");
		userDTO.setUserId(userId);
		userDTO.setAddresses(getAddressList());
	}

	@Test
	final void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(userDTO);
		
		UserRest userRest = userController.getUser(userId);
		assertNotNull(userRest);
		assertEquals(userId, userRest.getUserId());
		assertEquals(userDTO.getFirstName(), userRest.getFirstName());
		
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
