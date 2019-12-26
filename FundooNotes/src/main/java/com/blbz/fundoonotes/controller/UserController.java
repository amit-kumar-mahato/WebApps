package com.blbz.fundoonotes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blbz.fundoonotes.dto.LoginDetails;
import com.blbz.fundoonotes.dto.UserDto;
import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.responses.Response;
import com.blbz.fundoonotes.responses.UserAuthenticationResponse;
import com.blbz.fundoonotes.service.UserService;
import com.blbz.fundoonotes.utility.JwtGenerator;

@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtGenerator generate;

	/*
	 * API to register new user
	 */
	@PostMapping(value = "/users/register")
	@ResponseBody
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
	public ResponseEntity<UserAuthenticationResponse> login(@RequestBody LoginDetails loginDetails) {

		User userInformation = userService.login(loginDetails);

		if (userInformation!=null) {
			String token=generate.jwtToken(userInformation.getUserId());
			return ResponseEntity.status(HttpStatus.ACCEPTED).header("login successfull", loginDetails.getEmail())
					.body(new UserAuthenticationResponse(token, 200, loginDetails));

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserAuthenticationResponse("Login failed", 400, loginDetails));
		}

	}
	
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
	 * API to get all the records from DB
	 */
	@GetMapping("/users")
	public List<Map<String, Object>> getAll() {
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		List<User> list = userService.getAllDetails();

		Map<String, Object> map;
		for (User user : list) {
			map = new HashMap<String, Object>();
			map.put("UserId", user.getUserId());
			map.put("FirstName", user.getFirstName());
			map.put("LastName", user.getLastName());
			map.put("UserName", user.getUserName());
			map.put("ContactNumber", user.getMobile());
			userList.add(map);
		}
		return userList;
	}

	/*
	 * API to delete user details
	 */
	@DeleteMapping("/users/delete/{userId}")
	public ResponseEntity<Response> deleteDetails(@PathVariable long userId) {

		boolean result = userService.findByUserId(userId);
		LOGGER.info("Result :" + result);
		if (result) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("User Details is deleted successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response("The UserId you are searching is not available", 400, userId));
		}
	}

	/*
	 * API to get one user details
	 */
	@GetMapping("/users/{userId}")
	public Map<String, Object> getOneDetail(@PathVariable long userId) {
		return userService.findByIdUserId(userId);
	}

	@PutMapping("/users/update")
	public ResponseEntity<Response> updateUserDetails(@RequestBody User user) {
		boolean result = userService.updateDetails(user);
		if (result) {
			return ResponseEntity.status(HttpStatus.FOUND)
					.body(new Response("User Details is updated Successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response("Sorry UserId is not Available", 400, user.getUserId()));
		}
	}
}
