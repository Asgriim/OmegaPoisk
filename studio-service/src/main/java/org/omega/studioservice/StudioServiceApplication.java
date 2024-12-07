package org.omega.studioservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.omega.studioservice", "org.omega.common.reactive.security"})
public class StudioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudioServiceApplication.class, args);
    }

}
