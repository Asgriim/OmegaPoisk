package org.omega.omegapoisk.service.content;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.content.service.AnimeContentService;
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
class AnimeContentServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    AnimeContentService animeContentService;

    @Autowired
    AnimeRepository animeRepository;

    @Value("${spring.application.page-size}")
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
        animeRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetAllAnime() {
        animeContentService.create(new Anime(13));
        assertThat(animeContentService.countAll()).isEqualTo(1);

        animeContentService.create(new Anime(15));
        assertThat(animeContentService.countAll()).isEqualTo(2);

        animeContentService.create(new Anime(12));
        animeContentService.create(new Anime(12));
        assertThat(animeContentService.countAll()).isEqualTo(4);

    }

    @Test
    void shouldGetAnimeById() {
        Anime createdAnime = animeContentService.create(new Anime(13));
        Anime retrievedAnime = animeContentService.getById(createdAnime.getId());

        assertThat(retrievedAnime).isNotNull();
        assertThat(retrievedAnime.getId()).isEqualTo(createdAnime.getId());
        assertThat(retrievedAnime.getSeriesNum()).isEqualTo(createdAnime.getSeriesNum());
    }

    @Test
    void shouldUpdateAnime() {
        Anime createdAnime = animeContentService.create(new Anime(13));

        createdAnime.setSeriesNum(15);
        Anime retrievedAnime = animeContentService.update(createdAnime);

        assertThat(retrievedAnime).isNotNull();
        assertThat(retrievedAnime.getSeriesNum()).isEqualTo(createdAnime.getSeriesNum());
    }

    @Test
    void shouldDeleteAnime() {
        Anime createdAnime = animeContentService.create(new Anime(13));

        animeContentService.delete(createdAnime.getId());

        Anime retrievedAnime = animeContentService.getById(createdAnime.getId());

        assertThat(retrievedAnime).isNull();
        assertThat(animeContentService.countAll()).isEqualTo(0);
    }

    @Test
    void shouldGetContentCardById() {
        Anime createdAnime = animeContentService.create(new Anime(13));

        ContentCardDTO<Anime> cardDTO = animeContentService.getContentCardById(createdAnime.getId());

        assertThat(cardDTO).isNotNull();
        assertThat(cardDTO.getContent().getSeriesNum()).isEqualTo(createdAnime.getSeriesNum());
    }

    @Test
    void shouldGetAnimePage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(new Anime(10+i));
        }

        assertThat(animeContentService.getAnimePage(0)).hasSize(5);
        assertThat(animeContentService.getAnimePage(1)).hasSize(5);
        assertThat(animeContentService.getAnimePage(2)).hasSize(2);
        assertThat(animeContentService.getAnimePage(3)).hasSize(0);

    }

    @Test
    void shouldGetCardsPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(new Anime(10+i));
        }

        assertThat(animeContentService.getCardsPage(0)).hasSize(5);
        assertThat(animeContentService.getCardsPage(1)).hasSize(5);
        assertThat(animeContentService.getCardsPage(2)).hasSize(2);
        assertThat(animeContentService.getCardsPage(3)).hasSize(0);

    }


}