package com.blbz.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.blbz")
public class WebMvcConfiguration extends DelegatingWebMvcConfiguration {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resource = new InternalResourceViewResolver("/WEB-INF/JSP/", ".jsp");
		return resource; 
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/styles/**")
        .addResourceLocations("/WEB-INF/resources/css/");
	}

}
