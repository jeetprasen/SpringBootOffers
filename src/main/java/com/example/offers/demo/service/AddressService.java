package com.example.offers.demo.service;

import java.util.List;

import com.example.offers.demo.shared.dto.AddressDTO;

public interface AddressService {
	
	List<AddressDTO> getAddresses(String userId);
	
	AddressDTO getAddress(String id);
}
