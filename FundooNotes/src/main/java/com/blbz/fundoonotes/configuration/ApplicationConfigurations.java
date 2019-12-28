package com.blbz.fundoonotes.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigurations {

	@Bean
	public ModelMapper getModelMapper()
	{
		return new ModelMapper();
	}

}
