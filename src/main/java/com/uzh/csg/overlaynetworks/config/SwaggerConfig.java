package com.uzh.csg.overlaynetworks.config;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket fullApi() {
		return new Docket(SWAGGER_2)
				.groupName("full-api")
				.apiInfo(apiInfo())
				.select()
				.apis(any())
				.paths(regex("^.*$"))
				.build();
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Challenge Task REST API",
				"REST API for the Overlay Network's Challenge Task",
				"", "",
				new Contact("Communication Systems Research Group CSG, University of Zurich", "", ""),
				"", "");

		return apiInfo;
	}

}