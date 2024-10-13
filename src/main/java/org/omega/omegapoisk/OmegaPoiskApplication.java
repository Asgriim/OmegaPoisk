package org.omega.omegapoisk;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OmegaPoiskApplication  {

    public static void main(String[] args)  {
        SpringApplication.run(OmegaPoiskApplication.class, args);
    }
}
