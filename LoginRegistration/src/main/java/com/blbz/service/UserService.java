package com.blbz.service;

import org.springframework.stereotype.Component;

import com.blbz.entity.UserDetails;

@Component
public interface UserService {

	void computeSave(UserDetails user);

	boolean findByUserName(String uname);

	boolean authentication(String userName, String password);

}
