package com.blbz.fundoonotes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blbz.fundoonotes.model.Profile;

@Repository
public interface ProfilePicRepository extends CrudRepository<Profile, Long> {

	@Query(value = "select * from profile where user_id=?",nativeQuery = true)
	Profile findByUserId(long userId);
	
}
