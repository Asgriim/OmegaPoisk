package org.omega.reviewservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(
		security = @SecurityRequirement(name = "Bearer Authentication"),
		servers = {
				@Server(url = "/", description = "Gateway server")
		}
)
@SpringBootApplication
@ComponentScan(basePackages = {"org.omega.reviewservice", "org.omega.common.web.security", "org.omega.common.core.kafka"})
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

}
