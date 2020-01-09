package com.blbz.fundoonotes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.blbz.fundoonotes")).paths(PathSelectors.any()).build();
	}

	/*
	 * @Bean public Docket api() { return new
	 * Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any
	 * ()) .paths(PathSelectors.any()).build(); }
	 */
	
	/*
	 * @Bean public SwaggerSpringMvcPlugin customImplementation() {
	 * SwaggerSpringMvcPlugin ret = new
	 * SwaggerSpringMvcPlugin(this.springSwaggerConfig); return
	 * ret.useDefaultResponseMessages(false); }
	 */
}
