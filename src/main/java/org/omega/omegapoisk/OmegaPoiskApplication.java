package org.omega.omegapoisk;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.repository.content.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OmegaPoiskApplication implements CommandLineRunner {

    private final AnimeRepository animeRepository;

    public static void main(String[] args)  {
        SpringApplication.run(OmegaPoiskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<Anime> all = animeRepository.findAll();
        System.out.println(all);
        System.out.println(animeRepository.countAll());
    }
}
