package com.example.wx.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI swaggerOpenAPI() {

		return new OpenAPI()
				.info(new Info().title("Spring AI Alibaba PlayGround APIs")
						.contact(
								new Contact()
										.name("Spring AI Alibaba Community")
										.url("https://java2ai.com"))
						.description("Spring AI Alibaba PlayGround API Docs")
						.version("v1"));
	}

}