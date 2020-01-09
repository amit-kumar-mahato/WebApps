package com.blbz.fundoonotes.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserDto {

	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String userName;
	@NotBlank
	private String mobile;
	@NotBlank
	private String password;
	@NotBlank
	@Email
	private String email;

}
