package com.blbz.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.blbz.entity.UserDetails;

@Component
@Repository
@Transactional
public interface UserRepo extends JpaRepository<UserDetails, Long> {

	Optional<String> findOneByUserName(String uname);

	Optional<String> findOneByUserNameAndPassword(String userName, String password);

}
