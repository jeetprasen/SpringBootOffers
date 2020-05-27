package com.example.offers.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.offers.demo.exception.UserServiceException;
import com.example.offers.demo.io.entity.UserEntity;
import com.example.offers.demo.io.repositories.UserRepository;
import com.example.offers.demo.service.UserService;
import com.example.offers.demo.shared.dto.AddressDTO;
import com.example.offers.demo.shared.dto.UserDTO;
import com.example.offers.demo.shared.dto.Utils;
import com.example.offers.demo.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	Utils utils;
	
	@Override
	public UserDTO createUser(UserDTO user) {
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity storedUserDetails = userRepository.findUserByEmail(user.getEmail());
		
		if(storedUserDetails != null) {
			throw new UserServiceException("Recored Already Exists");
		}
		
		// Iterate for User Address
		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(10));
			user.getAddresses().set(i, address);
		}
		
		/*
		 * Copy details from DTO to User Entity class.
		 * The fields should match exactly b/w these two 
		 */
		UserEntity userEntity = new UserEntity();
		userEntity = modelMapper.map(user, UserEntity.class);
		
		String publicUserId = utils.generateUserId(10);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		
		storedUserDetails = userRepository.save(userEntity);
		
		UserDTO returnValue = new UserDTO();
		returnValue = modelMapper.map(storedUserDetails, UserDTO.class);
		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findUserByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}
	
	@Override
	public UserDTO getUser(String email) {
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDTO returnValue = new UserDTO();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDTO getUserByUserId(String userId) {
		UserDTO returnValue = new UserDTO();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			//throw new UsernameNotFoundException(userId);
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}		
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDTO updateUser(String userId, UserDTO user) {
		UserDTO returnValue = new UserDTO();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		// Update the required fields.
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			//throw new UsernameNotFoundException(userId);
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDTO> getUsers(int page, int limit) {
		
		List<UserDTO> returnValue = new ArrayList<>();
		
		if(page>0) {
			page = page - 1;
		}
		
		Pageable pagableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepository.findAll(pagableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for (UserEntity userEntity : users) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(userEntity, userDTO);
			returnValue.add(userDTO);
		}
		
		return returnValue;
	}
}
