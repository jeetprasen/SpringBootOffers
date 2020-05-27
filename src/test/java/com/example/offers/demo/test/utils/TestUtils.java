package com.example.offers.demo.test.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.offers.demo.io.entity.AddressEntity;
import com.example.offers.demo.io.entity.UserEntity;
import com.example.offers.demo.shared.dto.AddressDTO;
import com.example.offers.demo.shared.dto.UserDTO;
import com.example.offers.demo.shared.dto.Utils;

public class TestUtils {

	Utils utils;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	String password = "pswd";
	AddressDTO addressDTO;
	List<AddressDTO> addressList;
	UserDTO userDTO;
	
	

	public TestUtils(Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.utils = utils;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public List<UserEntity> generateRandomUsers(int numbers) {
		List<UserEntity> users = new ArrayList<UserEntity>(numbers);
		for (int i = 0; i < 10; i++) {
			UserEntity generatedUserEntity = generateRandonUser();
			users.add(generatedUserEntity);
		}
		return users;
	}

	private UserEntity generateRandonUser() {
		UserEntity userEntity = new UserEntity();
		// userEntity.setId(1234);
		userEntity.setFirstName(utils.generateStringOnly(5));
		userEntity.setLastName(utils.generateStringOnly(4));
		userEntity.setUserId(utils.generateUserId(10));
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(utils.generateAddressId(6)));
		userEntity.setEmail(utils.generateAddressId(5) + "@gmail.com");
		userEntity.setEmailVerificationStatus(utils.generateRandomBoolean());
		userEntity.setAddresses(getAddressEntity());
		return userEntity;
	}

	private List<AddressEntity> getAddressEntity() {
		List<AddressDTO> addresses = getAddressList();

		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		return new ModelMapper().map(addresses, listType);
	}

	private List<AddressDTO> getAddressList() {

		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setType("Shipping");
		addressDTO.setCountry("INDIA");
		addressDTO.setCity("Delhi");
		addressDTO.setState("DELHI");
		addressDTO.setPostalCode("101081");
		addressDTO.setStreetName("AnandpurDham");
		addressDTO.setAddressId(utils.generateAddressId(30));

		AddressDTO billingAddressDTO = new AddressDTO();
		billingAddressDTO.setType("billing");
		billingAddressDTO.setCountry("INDIA");
		billingAddressDTO.setCity("Delhi");
		billingAddressDTO.setState("DELHI");
		billingAddressDTO.setPostalCode("101081");
		billingAddressDTO.setStreetName("AnandpurDham");
		billingAddressDTO.setAddressId(utils.generateAddressId(30));

		addressList = new ArrayList<>();
		addressList.add(addressDTO);
		addressList.add(billingAddressDTO);

		return addressList;
	}
}
