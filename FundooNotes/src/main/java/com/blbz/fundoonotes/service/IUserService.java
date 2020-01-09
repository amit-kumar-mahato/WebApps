package com.blbz.fundoonotes.service;

import java.util.List;
import java.util.Map;

import com.blbz.fundoonotes.dto.LoginDetails;
import com.blbz.fundoonotes.dto.Updatepassword;
import com.blbz.fundoonotes.dto.UserDto;
import com.blbz.fundoonotes.model.User;

public interface IUserService {

	User registration(UserDto user);

	List<User> getAllDetails();

	Map<String, Object> findByIdUserId(long userId);

	boolean updateDetails(User user);

	boolean findByUserId(long userId);

	User login(LoginDetails loginDetails);

	boolean verify(String token);

	boolean isUserAvailable(String email);

	boolean updatePassword(String token, Updatepassword pswd);

	List<User> getAllUsers();

}
