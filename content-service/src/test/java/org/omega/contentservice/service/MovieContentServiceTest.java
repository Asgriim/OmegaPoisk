package org.omega.contentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.contentservice.dto.GameDTO;
import org.omega.contentservice.dto.MovieDTO;
import org.omega.contentservice.entity.Movie;
import org.omega.contentservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MovieContentServiceTest {

    @Mock
    AvgRatingService avgRatingService;

    private MovieContentService movieContentService;

    @Autowired
    private MovieRepository movieRepository;

    static Movie movie;

    @Value("${spring.application.page}")
    int pageSize;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    @BeforeEach
    void beforeEach() {
        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);
        movieContentService = new MovieContentService(movieRepository, avgRatingService);

        movie = new Movie(220);
        movie.setId(0L);
        movie.setTitle("Test content");
        movie.setDescription("Test desc");
        movie.setNew(true);
        movieRepository.deleteAll();
    }

    @Test
    void entityTest() {
        movie.setNew(false);
        assertThat(new MovieDTO(movie).toEntity()).isEqualTo(movie);
    }

    @Test
    void getContentPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            movieContentService.create(movie);
        }

        assertThat(movieContentService.getContentPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(movieContentService.getContentPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(movieContentService.getContentPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(movieContentService.getContentPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getContentPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            movieContentService.create(movie);
        }

        assertThat(
                movieContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                movieContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getById() {
        Movie saved = movieRepository.save(movie);

        assertThat(movieContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getTitle()).isEqualTo(movie.getTitle());
                    assertThat(m.getDuration()).isEqualTo(movie.getDuration());
                });
    }

    @Test
    void create() {
        Movie saved = movieContentService.create(movie);

        assertThat(movieContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getTitle()).isEqualTo(movie.getTitle());
                    assertThat(m.getDuration()).isEqualTo(movie.getDuration());
                });
    }

    @Test
    void update() {
        Movie saved = movieContentService.create(movie);

        saved.setTitle("Changed");
        saved.setNew(false);

        movieContentService.update(saved);

        assertThat(movieContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getTitle()).isEqualTo(movie.getTitle());
                    assertThat(m.getDuration()).isEqualTo(movie.getDuration());
                });
    }

    @Test
    void delete() {
        Movie saved = movieContentService.create(movie);

        movieContentService.delete(saved.getId());

        assertThat(movieContentService.getById(saved.getId())).isNull();
    }

    @Test
    void testDelete() {
        Movie saved = movieContentService.create(movie);

        movieContentService.delete(saved);

        assertThat(movieContentService.getById(saved.getId())).isNull();
    }

    @Test
    void count() {
        movieContentService.create(movie);
        assertThat(movieContentService.count()).isEqualTo(1);

        movieContentService.create(movie);
        assertThat(movieContentService.count()).isEqualTo(2);

        movieContentService.create(movie);
        movieContentService.create(movie);
        assertThat(movieContentService.count()).isEqualTo(4);
    }

    @Test
    void getCardPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            movieContentService.create(movie);
        }

        assertThat(movieContentService.getCardPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(movieContentService.getCardPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(movieContentService.getCardPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(movieContentService.getCardPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getCardPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            movieContentService.create(movie);
        }

        assertThat(
                movieContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                movieContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getCardById() {
        Movie saved = movieContentService.create(movie);

        assertThat(movieContentService.getCardById(saved.getId()))
                .isNotNull()
                .satisfies(m -> {
                    assertThat(m.getAvgRating()).isEqualTo(5.0);
                    assertThat(m.getContent().getTitle()).isEqualTo(movie.getTitle());
                    assertThat(m.getContent().getDuration()).isEqualTo(movie.getDuration());
                });
    }
}
