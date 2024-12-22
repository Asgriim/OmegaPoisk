package org.omega.posterservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Gateway server")
        }
)
@SpringBootApplication
public class PosterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosterServiceApplication.class, args);
    }

}
