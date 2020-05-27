package com.example.offers.demo.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.offers.demo.exception.UserServiceException;
import com.example.offers.demo.service.AddressService;
import com.example.offers.demo.service.UserService;
import com.example.offers.demo.shared.dto.AddressDTO;
import com.example.offers.demo.shared.dto.UserDTO;
import com.example.offers.demo.ui.model.request.UserDetailsRequestModel;
import com.example.offers.demo.ui.model.response.AddressesRest;
import com.example.offers.demo.ui.model.response.ErrorMessages;
import com.example.offers.demo.ui.model.response.OperationStatusModel;
import com.example.offers.demo.ui.model.response.RequestOperationStatus;
import com.example.offers.demo.ui.model.response.UserRest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// http://localhost:8080/users

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {

		UserRest returnValue = new UserRest();

		UserDTO userDTO = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDTO, returnValue);

		return returnValue;
	}

	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserRest returnValue = new UserRest();

			/*
			 * Custom methods to raise exception
			 * Used More better method for Bean/Object Mapping (BeanUtil.copyProperties method is not able to map class inside a class
			 * 
			 */
				if (userDetails.getFirstName().isEmpty()) {
					// throw new Exception(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
					// Custom user service exception
					throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELDS.getErrorMessage());
				}
		
				if (userDetails.getLastName().isEmpty()) {
					throw new NullPointerException("Last Name cannot be NULL");
				}

		UserDTO userDTO;
		//BeanUtils.copyProperties(userDetails, userDTO);
		ModelMapper modelMapper = new ModelMapper();
		userDTO = modelMapper.map(userDetails, UserDTO.class);
		
		UserDTO createdUser = userService.createUser(userDTO);
		//BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userDetails, userDTO);

		UserDTO updateUser = userService.updateUser(id, userDTO);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<UserRest>();
		List<UserDTO> users = userService.getUsers(page, limit);

		for (UserDTO userDTO : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDTO, userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}
	
	// http://localhost:8080/users/<user id>/addresses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id) {

		List<AddressesRest> addressesListRestModel = new ArrayList<>();
		
		List<AddressDTO> addressDTO = addressService.getAddresses(id);
		
		if(addressDTO != null && !addressDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			
			addressesListRestModel = new ModelMapper().map(addressDTO, listType);
			
			for (AddressesRest addressesRest : addressesListRestModel) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(addressesRest.getAddressId(), id)).withSelfRel();
				addressesRest.add(addressLink);
				
				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				addressesRest.add(userLink);
			}
		}
		
		return new CollectionModel<>(addressesListRestModel);
	}
	
	
	// http://localhost:8080/users/<user id>/<address id>
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public EntityModel<AddressesRest> getUserAddress(@PathVariable String addressId, @PathVariable String userId) {

		AddressDTO addressDTO = addressService.getAddress(addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		
		/*  
		 * 	Adding link to the response
		 *  Link addressLink = linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
		 *	Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
		 *	Link addressesLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");
		 *
		 */
		
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		
		
		AddressesRest addressRestModel = modelMapper.map(addressDTO, AddressesRest.class);
		
		addressRestModel.add(addressLink);
		addressRestModel.add(userLink);
		addressRestModel.add(addressesLink);
		
		return new EntityModel<> (addressRestModel);
		
	}	
}
