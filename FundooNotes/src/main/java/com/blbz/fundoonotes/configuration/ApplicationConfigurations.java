package com.blbz.fundoonotes.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blbz.fundoonotes.model.Note;

@Configuration
public class ApplicationConfigurations {

	@Bean
	public ModelMapper getModelMapper()
	{
		return new ModelMapper();
	}

	@Bean
	public Note getNote() {
		return new Note();
	}
}
