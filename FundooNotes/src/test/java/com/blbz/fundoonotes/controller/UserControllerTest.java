package com.blbz.fundoonotes.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.blbz.fundoonotes.model.User;
import com.blbz.fundoonotes.serviceimpl.UserServiceImpl;

@WebMvcTest(value = UserController.class)
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testRegistration() {
		User mockUser = new User();
		
	}
}
