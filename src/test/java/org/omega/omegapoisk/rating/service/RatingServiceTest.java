package org.omega.omegapoisk.rating.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.rating.entity.Rating;
import org.omega.omegapoisk.rating.repository.RatingRepository;
import org.omega.omegapoisk.user.entity.RoleEntity;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.user.repository.RoleRepository;
import org.omega.omegapoisk.user.repository.UserRepository;
import org.omega.omegapoisk.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class RatingServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AnimeContentService animeContentService;

    @Autowired
    AnimeRepository animeRepository;

    @Value("${spring.application.page}")
    int pageSize;

    private User user;
    private Anime anime;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        userRepository.deleteAll();

        RoleEntity roleEntity = roleRepository.findById(1L).orElseThrow(InvaliUserOrPasswordException::new);
        userService.registerUser(new User(0, "aboba", "abobapass", "aboba@mail.ru", 1, roleEntity));

        user = userService.loadUserByUsername("aboba");
        anime = animeContentService.create(new Anime(13));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreate() {
        int userId = (int) user.getId();
        int contentId = anime.getId().intValue();
        ratingService.create(new Rating(0, 3, userId, contentId));
        assertThat(ratingService.findByUserAndContentId(userId, contentId).getValue()).isEqualTo(3);
    }

    @Test
    void shouldUpdate() {
        int userId = (int) user.getId();
        int contentId = anime.getId().intValue();
        ratingService.create(new Rating(0, 3, userId, contentId));
        ratingService.update(new Rating(0, 4, userId, contentId));
        assertThat(ratingService.findByUserAndContentId(userId, contentId).getValue()).isEqualTo(4);
    }

    @Test
    void shouldDelete() {
        int userId = (int) user.getId();
        int contentId = anime.getId().intValue();
        Rating rating = ratingService.create(new Rating(0, 3, userId, contentId));
        ratingService.delete(rating.getId());
        assertThat(ratingService.findByUserAndContentId(userId, contentId)).isNull();

    }

}