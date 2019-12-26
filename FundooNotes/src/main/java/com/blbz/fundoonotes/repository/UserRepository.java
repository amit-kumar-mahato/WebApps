package com.blbz.fundoonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blbz.fundoonotes.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findFirstByOrderByUserIdDesc();

	Optional<User> findOneByUserIdAndPassword(long userId, Object pswd);

	User findOneByEmail(String email);

	//User getUser(String email);

	
	/*
	 * @Query(value = "update User set isVerified=true where userId = :id") boolean
	 * verify(@Param("id") long id);
	 */

}
