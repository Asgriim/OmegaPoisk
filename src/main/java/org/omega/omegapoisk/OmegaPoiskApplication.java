package org.omega.omegapoisk;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.entity.user.RoleEntity;
import org.omega.omegapoisk.entity.user.User;
import org.omega.omegapoisk.repository.content.AnimeRepository;
import org.omega.omegapoisk.repository.user.RoleRepository;
import org.omega.omegapoisk.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OmegaPoiskApplication implements CommandLineRunner {

    //todo DELETE ALL
    private final AnimeRepository animeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public static void main(String[] args)  {
        SpringApplication.run(OmegaPoiskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Iterable<RoleEntity> all = roleRepository.findAll();
        System.out.println(all);
        Iterable<User> all1 = userRepository.findAll();
        System.out.println(all1);
    }
}
