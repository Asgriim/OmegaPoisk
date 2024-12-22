package org.omega.studioservice;

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
@ComponentScan(basePackages = {"org.omega.studioservice", "org.omega.common.reactive.security"})
public class StudioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudioServiceApplication.class, args);
    }

}
