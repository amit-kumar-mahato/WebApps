package com.blbz.fundoonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class UserController {

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
	@ApiOperation(value = "Api to register user", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "registration successfull"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<Response> registration(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400, null));
		} else {
			User user = userService.registration(userDto);
			return user != null
					? ResponseEntity.status(HttpStatus.CREATED)
							.body(new Response("registration successfull", 200, user))
					: ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
							.body(new Response("user already exist", 400, user));
		}
	}

	/*
	 * Api for user authentication
	 */
	@PostMapping("/users/login")
	@ApiOperation(value = "Api for Login", response = Response.class)
	public ResponseEntity<UserAuthenticationResponse> login(@RequestBody LoginDetails loginDetails) {

		User userInformation = userService.login(loginDetails);
		loginDetails.setPassword("******");
		if (userInformation != null) {
			String token = generate.jwtToken(userInformation.getUserId());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new UserAuthenticationResponse(token, 200, loginDetails));
		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new UserAuthenticationResponse("Login failed", 404, loginDetails));
		}
	}

	/*
	 * API to verify the user
	 */
	@GetMapping("/users/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) {

		log.info("token for verification" + token);
		boolean update = userService.verify(token);
		return (update) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("verified", 200))
				: ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("not verified", 400));
	}

	/*
	 * API for forgot password
	 */
	@ApiOperation(value = "Api for forgotpassword", response = Response.class)
	@PostMapping("users/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) {
		log.info("Email :" + email);

		boolean result = userService.isUserAvailable(email);
		return (result) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("User Exist", 200))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User Doesn't Exist", 400));
	}

	/*
	 * API to update password
	 */
	@PutMapping("users/updatepassword/{token}")
	@ApiOperation(value = "Api for update password", response = Response.class)
	public ResponseEntity<Response> updatePassword(@Valid @PathVariable("token") String token,
			@RequestBody Updatepassword pswd, BindingResult bindingResult) {
		log.info("Token :" + token);
		log.info("New Password :" + pswd.getNewPassword());

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400, null));
		} 
			User userInfo = userService.updatePassword(token, pswd);
			userInfo.setPswd("*******");
			
			return userInfo != null
					? ResponseEntity.status(HttpStatus.OK)
							.body(new Response("Password is Update Successfully", 200, userInfo))
					: ResponseEntity.status(HttpStatus.NOT_MODIFIED)
							.body(new Response("Password and Confirm Password doesn't matched", 400));
		/*
		 * boolean result = userService.updatePassword(token, pswd); return (result) ?
		 * ResponseEntity.status(HttpStatus.ACCEPTED).body(new
		 * Response("Password is Update Successfully", 200)) :
		 * ResponseEntity.status(HttpStatus.NOT_MODIFIED) .body(new
		 * Response("Password and Confirm Password doesn't matched", 400));
		 */
	}

	/*
	 * API to get User list
	 */
	@GetMapping("users")
	@ApiOperation(value = "Api to get user list", response = Response.class)
	public ResponseEntity<Response> usersList() {
		List<User> userList = (List<User>) userRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Users are", 200, userList));
	}
}
