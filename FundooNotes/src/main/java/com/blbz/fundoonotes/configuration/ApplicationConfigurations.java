package com.blbz.fundoonotes.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blbz.fundoonotes.model.Collaborator;
import com.blbz.fundoonotes.model.Label;
import com.blbz.fundoonotes.model.Note;

@Configuration
public class ApplicationConfigurations {

	@Bean
	public BCryptPasswordEncoder getPasswordEncryption() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper getModelMapper()
	{
		return new ModelMapper();
	}

	@Bean
	public Note getNote() {
		return new Note();
	}
	
	@Bean
	public Label getLabel() {
		return new Label();
	}
}
