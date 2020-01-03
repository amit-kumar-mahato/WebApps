package com.blbz.fundoonotes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.LoginDetails;
import com.blbz.fundoonotes.dto.Updatepassword;
import com.blbz.fundoonotes.dto.UserDto;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.repository.UserRepository;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.responses.UserAuthenticationResponse;
import com.blbz.fundoonotes.service.IUserService;
import com.blbz.fundoonotes.utility.JwtGenerator;

@RestController
//@Slf4j
public class UserController {
	
	private final Logger LOGGER  = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private JwtGenerator generate;
	
	@Autowired
	private UserRepository userRepository;

	/*
	 * API to register new user
	 */
	@PostMapping(value = "/users/register")
	public ResponseEntity<Response> registration(@RequestBody UserDto user) {
		/*
		 * HashMap<String, String> map = new HashMap<String, String>();
		 * map.put("UserId", userService.registration(user)); return map;
		 */

		boolean result = userService.registration(user);
		if (result) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("registration successfull", 200, user));

		} else {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new Response("user already exist", 400, user));
		}
	}
	
	/*
	 * Api for user authentication
	 * */
	@PostMapping("/users/login")
	@CachePut(key = "#token", value = "userId")
	public ResponseEntity<UserAuthenticationResponse> login(@RequestBody LoginDetails loginDetails) {

		User userInformation = userService.login(loginDetails);
		loginDetails.setPassword("******");
		if (userInformation!=null) {
			String token=generate.jwtToken(userInformation.getUserId());
			return ResponseEntity.status(HttpStatus.ACCEPTED).header("login successfull", loginDetails.getEmail())
					.body(new UserAuthenticationResponse(token, 200, loginDetails));
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserAuthenticationResponse("Login failed", 400, loginDetails));
		}
	}
	
	/*
	 * API to verify the user
	 * */
	@GetMapping("/users/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) throws Exception {

		System.out.println("token for verification" + token);
		boolean update = userService.verify(token);
		if (update) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("verified", 200));
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("not verified", 400));

		}
	}
	
	/*
	 * API for forgot password
	 * */
	@PostMapping("users/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email){
		LOGGER.info("Email :"+email);
		
		boolean result = userService.isUserAvailable(email);
		if(result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("User Exist", 200));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User Doesn't Exist", 400));
		}
	}

	/*
	 * API to update password
	 * */
	@PostMapping("users/updatepassword/{token}")
	public ResponseEntity<Response> updatePassword(@PathVariable("token") String token, @RequestBody Updatepassword pswd) throws Exception{
		LOGGER.info("Token :"+token);
		LOGGER.info("New Password :"+pswd.getNewPassword());
		
		boolean result = userService.updatePassword(token,pswd);
		
		if(result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Password is Update Successfully", 200));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new Response("Password and Confirm Password doesn't matched", 400));
		}
	}
	
	/*
	 * API to get User list
	 * */
	@GetMapping("users")
	public ResponseEntity<Response> usersList(){
		List<User> userList = (List<User>) userRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Users are", 200,userList));
	}
}
