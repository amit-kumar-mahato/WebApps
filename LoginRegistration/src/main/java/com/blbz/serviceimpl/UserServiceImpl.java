package com.blbz.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.entity.UserDetails;
import com.blbz.repository.UserRepo;
import com.blbz.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepo userRepo;

	@Override
	public void computeSave(UserDetails user) {
		userRepo.save(user);
	}

	@Override
	public boolean findByUserName(String uname) {
		Optional<String> isUserNameAvailable = userRepo.findOneByUserName(uname);
		if(isUserNameAvailable.isPresent()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean authentication(String userName, String password) {
		
		Optional<String> isUserValid = userRepo.findOneByUserNameAndPassword(userName,password);
		if(isUserValid.isPresent()) {
			return true;
		}
		return false;
	}

}
