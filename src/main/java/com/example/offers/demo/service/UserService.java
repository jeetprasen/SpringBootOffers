package com.example.offers.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.offers.demo.shared.dto.UserDTO;

public interface UserService extends UserDetailsService{
	UserDTO createUser(UserDTO user);
	
	// Using this so as to be able to add the user Id to the header after validation
	UserDTO getUser(String email);

	UserDTO getUserByUserId(String id);

	UserDTO updateUser(String id, UserDTO userDTO);

	void deleteUser(String userId);

	List<UserDTO> getUsers(int page, int limit);
}
