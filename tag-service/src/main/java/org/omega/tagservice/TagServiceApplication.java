package org.omega.tagservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.omega.tagservice", "org.omega.common.reactive.security"})
public class TagServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TagServiceApplication.class, args);
	}

}
