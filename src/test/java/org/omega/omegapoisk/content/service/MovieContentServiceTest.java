package org.omega.omegapoisk.content.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.ContentCard;
import org.omega.omegapoisk.content.entity.Movie;
import org.omega.omegapoisk.content.repository.MovieRepository;
import org.omega.omegapoisk.content.service.MovieContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class MovieContentServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    MovieContentService movieContentService;

    @Autowired
    MovieRepository movieRepository;

    @Value("${spring.application.page}")
    int pageSize;


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
        movieRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetAllMovie() {
        movieContentService.create(new Movie(90));
        assertThat(movieContentService.count()).isEqualTo(1);

        movieContentService.create(new Movie(90));
        assertThat(movieContentService.count()).isEqualTo(2);

        movieContentService.create(new Movie(90));
        movieContentService.create(new Movie(90));
        assertThat(movieContentService.count()).isEqualTo(4);

    }

    @Test
    void shouldGetMovieById() {
        Movie createdMovie = movieContentService.create(new Movie(90));
        Movie retrievedMovie = movieContentService.getById(createdMovie.getId());

        assertThat(retrievedMovie).isNotNull();
        assertThat(retrievedMovie.getId()).isEqualTo(createdMovie.getId());
        assertThat(retrievedMovie.getDuration()).isEqualTo(createdMovie.getDuration());
    }

    @Test
    void shouldUpdateMovie() {
        Movie createdMovie = movieContentService.create(new Movie(90));

        createdMovie.setDuration(60);
        Movie retrievedMovie = movieContentService.update(createdMovie);

        assertThat(retrievedMovie).isNotNull();
        assertThat(retrievedMovie.getDuration()).isEqualTo(createdMovie.getDuration());
    }

    @Test
    void shouldDeleteMovie() {
        Movie createdMovie = movieContentService.create(new Movie(90));

        movieContentService.delete(createdMovie.getId());

        Movie retrievedMovie = movieContentService.getById(createdMovie.getId());

        assertThat(retrievedMovie).isNull();
        assertThat(movieContentService.count()).isEqualTo(0);
    }

    @Test
    void shouldGetContentCardById() {
        Movie createdMovie = movieContentService.create(new Movie(90));

        ContentCard<Movie> card = movieContentService.getCardById(createdMovie.getId());

        assertThat(card).isNotNull();
        assertThat(card.getContent().getDuration()).isEqualTo(createdMovie.getDuration());
    }

    @Test
    void shouldGetMoviePage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            movieContentService.create(new Movie(90));
        }

        assertThat(movieContentService.getContentPage(PageRequest.of(0, pageSize))).hasSize(5);
        assertThat(movieContentService.getContentPage(PageRequest.of(1, pageSize))).hasSize(5);
        assertThat(movieContentService.getContentPage(PageRequest.of(2, pageSize))).hasSize(2);
        assertThat(movieContentService.getContentPage(PageRequest.of(3, pageSize))).hasSize(0);

    }

    @Test
    void shouldGetCardsPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            movieContentService.create(new Movie(90));
        }

        assertThat(movieContentService.getCardPage(PageRequest.of(0, pageSize))).hasSize(5);
        assertThat(movieContentService.getCardPage(PageRequest.of(1, pageSize))).hasSize(5);
        assertThat(movieContentService.getCardPage(PageRequest.of(2, pageSize))).hasSize(2);
        assertThat(movieContentService.getCardPage(PageRequest.of(3, pageSize))).hasSize(0);

    }


}