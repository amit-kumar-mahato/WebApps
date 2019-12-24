package com.blbz.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blbz.dto.UserDto;
import com.blbz.entity.LoginDetails;
import com.blbz.entity.UserDetails;
import com.blbz.service.UserService;

@Controller
// @RequestMapping("/v1")
public class UserController {

	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	private ModelMapper modelMapper = new ModelMapper();

	private ModelAndView mv;

	@RequestMapping("/")
	public String indexPage() {
		return "index";
	}

	@RequestMapping("/reg")
	public String reg() {
		return "registration";
	}

	@ModelAttribute("regForm")
	public UserDto details() {
		return new UserDto();
	}

	@ModelAttribute("loginForm")
	public LoginDetails login() {
		return new LoginDetails();
	}

	@PostMapping("/register")
	public ModelAndView register(@ModelAttribute("regForm") UserDto user) {

		UserDetails userDetails = modelMapper.map(user, UserDetails.class);
		LOGGER.info(user.getPassword());
		LOGGER.info(userDetails.getPassword());
		userService.computeSave(userDetails);
		String message = "User Details Added successfully!!!";

		mv = new ModelAndView("index");
		mv.addObject("insert", message);
		return mv;
	}

	@PostMapping("/checkuseravailability")
	@ResponseBody
	public String userAvailability(@RequestParam("uname") String uname) {
		System.out.println(uname);
		boolean result = userService.findByUserName(uname);

		if (result) {
			return "UserName is Already Exist!!!";
		}
		return null;
	}
	
	@PostMapping("/login")
	public ModelAndView authentication(@RequestParam("userName") String userName, @RequestParam("password") String password) {
		boolean result = userService.authentication(userName,password);
		
		if(result) {
			String message = "Welcome "+userName;
			mv = new ModelAndView("welcome");
			mv.addObject("success",message);
			return mv;
		}else {
			String message = "Invalid Username/Password";
			mv = new ModelAndView("index");
			mv.addObject("invalid", message);
			return mv;
		}
		
	}

}
