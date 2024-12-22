package org.omega.contentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.controller.AnimeController;
import org.omega.contentservice.dto.AnimeDTO;
import org.omega.contentservice.entity.Anime;
import org.omega.contentservice.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@MockBean(AnimeController.class)
@MockBean(KafkaProducerService.class)
@Testcontainers
class AnimeContentServiceTest {

    @Mock
    AvgRatingService avgRatingService;

    private AnimeContentService animeContentService;

    @Autowired
    private AnimeRepository animeRepository;

    static Anime anime;

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
        animeContentService = new AnimeContentService(animeRepository, avgRatingService);

        anime = new Anime(220);
        anime.setId(0L);
        anime.setTitle("Test content");
        anime.setDescription("Test desc");
        anime.setNew(true);
        animeRepository.deleteAll();
    }

    @Test
    void entityTest() {
        anime.setNew(false);
        assertThat(new AnimeDTO(anime).toEntity()).isEqualTo(anime);
    }

    @Test
    void getContentPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(anime);
        }

        assertThat(animeContentService.getContentPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(animeContentService.getContentPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(animeContentService.getContentPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(animeContentService.getContentPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getContentPageRange() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(anime);
        }

        assertThat(
                animeContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                animeContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);

    }

    @Test
    void getById() {
        Anime saved = animeRepository.save(anime);

        assertThat(animeContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getTitle()).isEqualTo(anime.getTitle());
                    assertThat(a.getSeriesNum()).isEqualTo(anime.getSeriesNum());
                });
    }

    @Test
    void create() {
        Anime saved = animeContentService.create(anime);

        assertThat(animeContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getTitle()).isEqualTo(anime.getTitle());
                    assertThat(a.getSeriesNum()).isEqualTo(anime.getSeriesNum());
                });
    }

    @Test
    void update() {
        Anime saved = animeContentService.create(anime);

        saved.setTitle("Changed");
        saved.setNew(false);

        animeContentService.update(saved);

        assertThat(animeContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getTitle()).isEqualTo(anime.getTitle());
                    assertThat(a.getSeriesNum()).isEqualTo(anime.getSeriesNum());
                });
    }

    @Test
    void delete() {
        Anime saved = animeContentService.create(anime);

        animeContentService.delete(saved.getId());

        assertThat(animeContentService.getById(saved.getId())).isNull();
    }

    @Test
    void testDelete() {
        Anime saved = animeContentService.create(anime);

        animeContentService.delete(saved);

        assertThat(animeContentService.getById(saved.getId())).isNull();
    }

    @Test
    void count() {
        animeContentService.create(anime);
        assertThat(animeContentService.count()).isEqualTo(1);

        animeContentService.create(anime);
        assertThat(animeContentService.count()).isEqualTo(2);

        animeContentService.create(anime);
        animeContentService.create(anime);
        assertThat(animeContentService.count()).isEqualTo(4);
    }

    @Test
    void getCardPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(anime);
        }

        assertThat(animeContentService.getCardPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(animeContentService.getCardPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(animeContentService.getCardPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(animeContentService.getCardPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getCardPageRange() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(anime);
        }

        assertThat(
                animeContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                animeContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getCardById() {
        Anime saved = animeContentService.create(anime);

        assertThat(animeContentService.getCardById(saved.getId()))
                .isNotNull()
                .satisfies(a -> {
                    assertThat(a.getAvgRating()).isEqualTo(5.0);
                    assertThat(a.getContent().getTitle()).isEqualTo(anime.getTitle());
                    assertThat(a.getContent().getSeriesNum()).isEqualTo(anime.getSeriesNum());
                });
    }
}