package org.omega.omegapoisk.studio.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.studio.entity.Studio;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.studio.repository.StudioRepository;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class StudioServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    StudioService studioService;

    @Autowired
    StudioRepository studioRepository;

    @Autowired
    AnimeContentService animeContentService;

    @Autowired
    AnimeRepository animeRepository;

    @Value("${spring.application.page}")
    int pageSize;

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
        studioRepository.deleteAll();
        animeRepository.deleteAll();

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
        studioService.createStudio(new Studio(0, "Studio 1"));
        assertThat(studioService.getAllStudios()).hasSize(1);

        studioService.createStudio(new Studio(0, "Studio 2"));
        assertThat(studioService.getAllStudios()).hasSize(2);

        studioService.createStudio(new Studio(0, "Studio 3"));
        studioService.createStudio(new Studio(0, "Studio 4"));
        assertThat(studioService.getAllStudios()).hasSize(4);
    }

    @Test
    void shouldGetStudioById() {
        Studio studio = studioService.createStudio(new Studio(0, "Studio 1"));
        assertThat(studioService.getStudioById(studio.getId())).isNotNull();
        assertThat(studioService.getStudioById(studio.getId()).getName()).isEqualTo("Studio 1");
    }

    @Test
    void shouldAddStudioToContent() {
        Studio studio = studioService.createStudio(new Studio(0, "Studio 1"));
        studioService.addContentToStudio(studio.getId(), anime.getId());

        assertThat(studioService.findByContentId(anime.getId())).isEqualTo(List.of(studio));
    }

    @Test
    void shouldDeleteByContent() {
        Studio studio = studioService.createStudio(new Studio(0, "Studio 1"));
        studioService.addContentToStudio(studio.getId(), anime.getId());

        studioService.deleteContentFromStudio(studio.getId(), anime.getId());
        assertThat(studioService.findByContentId(anime.getId())).isEqualTo(List.of());

    }

    @Test
    void shouldDelete() {
        Studio studio = studioService.createStudio(new Studio(0, "Studio 1"));
        studioService.addContentToStudio(studio.getId(), anime.getId());

        studioService.deleteStudio(studio.getId());
        assertThat(studioService.getAllStudios()).hasSize(0);

    }

}