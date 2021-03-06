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

	Optional<User> findOneByUserIdAndPswd(long userId, Object pswd);

	Optional<User> findOneByEmail(String email);

	User findOneByUserId(long userId);

	//User findoneByUserId(long userId);

	//User getUser(String email);

	
	/*
	 * @Query(value = "update User set isVerified=true where userId = :id") boolean
	 * verify(@Param("id") long id);
	 */

}
