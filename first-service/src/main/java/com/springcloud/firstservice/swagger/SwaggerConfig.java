package com.springcloud.firstservice.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@OpenAPIDefinition(
	info = @Info(title = "API Documentation", version = "1.0", description = "Documentation API v1.0")
)
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {

		return new OpenAPI()
			.addServersItem(new Server().url("/first-service"));
	}
}
