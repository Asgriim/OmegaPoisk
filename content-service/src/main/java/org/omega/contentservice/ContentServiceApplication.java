package org.omega.contentservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(
        security = @SecurityRequirement(name = "Bearer Authentication"),
        servers = {
                @Server(url = "/", description = "Gateway server")
        }
)
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"org.omega.contentservice", "org.omega.common.web.security", "org.omega.common.core.kafka"})
public class ContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }

}
