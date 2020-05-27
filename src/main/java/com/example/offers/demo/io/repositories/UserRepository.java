package com.example.offers.demo.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.offers.demo.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	// Here find and By is very important to keep while defining methods here.
	UserEntity findUserByEmail(String email);
	UserEntity findByUserId(String userId);
	
	// UserEntity findByLastName(String lastName);
	// UserEntity findByEmail(String email);
	
	@Query(value="select * from offers o where o.email_verification_status = 'true'",
			countQuery = "select count(*) from offers o where o.email_verification_status = 'true'", nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pagableRequest);
	
	@Query(value="select * from offers o where o.first_name = ?1", nativeQuery = true)
	List<UserEntity> findUsersByFirstName(String firstName);
	
	@Query(value="select * from offers o where o.last_name = :lastName", nativeQuery = true)
	List<UserEntity> findUsersByLastName(@Param("lastName") String lastName);
	
	@Query(value="select * from offers o where o.first_name LIKE %:keyword% or o.last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);
	
	@Query(value="select o.first_name, o.last_name from offers o where o.first_name LIKE %:keyword% or o.last_name LIKE %:keyword%", nativeQuery = true)
	List<Object[]> findUsersFirstAndLastNameByKeyword(@Param("keyword") String keyword);
	
	@Modifying
	@Transactional
	@Query(value="update offers o set o.email_verification_status=:emailVerificationStatus where o.user_id=:userId", nativeQuery = true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
	
	
	// JPQL queries
	@Query(value="select user from UserEntity user where user.userId = :userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	@Query(value="select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
	List<Object[]> findUserFullNameByUserId(@Param("userId") String userId);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE UserEntity user set user.emailVerificationStatus=:emailVerificationStatus where user.userId=:userId")
	void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);
	
}

