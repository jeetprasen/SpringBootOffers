package com.example.offers.demo.io.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.offers.demo.io.entity.AddressEntity;
import com.example.offers.demo.io.entity.UserEntity;

/*
 * Here we user for generics type in CrudRepository as 
 * AddressEntity and Long - (which is the primary key for that Database)
 * 
 */

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>{

	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId); 
	
}
