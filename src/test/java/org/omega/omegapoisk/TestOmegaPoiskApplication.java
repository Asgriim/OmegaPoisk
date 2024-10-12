package org.omega.omegapoisk;

import org.springframework.boot.SpringApplication;

public class TestOmegaPoiskApplication {

    public static void main(String[] args) {
        SpringApplication.from(OmegaPoiskApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
