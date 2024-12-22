package org.omega.contentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.controller.AnimeController;
import org.omega.contentservice.controller.ComicController;
import org.omega.contentservice.dto.AnimeDTO;
import org.omega.contentservice.dto.ComicDTO;
import org.omega.contentservice.entity.Comic;
import org.omega.contentservice.repository.ComicRepository;
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
@MockBean(ComicController.class)
@MockBean(KafkaProducerService.class)
@Testcontainers
class ComicContentServiceTest {

    @Mock
    AvgRatingService avgRatingService;

    private ComicContentService comicContentService;

    @Autowired
    private ComicRepository comicRepository;

    static Comic comic;

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
        comicContentService = new ComicContentService(comicRepository, avgRatingService);

        comic = new Comic(true, 220);
        comic.setId(0L);
        comic.setTitle("Test content");
        comic.setDescription("Test desc");
        comic.setNew(true);
        comicRepository.deleteAll();
    }

    @Test
    void entityTest() {
        comic.setNew(false);
        assertThat(new ComicDTO(comic).toEntity()).isEqualTo(comic);
    }

    @Test
    void getContentPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            comicContentService.create(comic);
        }

        assertThat(comicContentService.getContentPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(comicContentService.getContentPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(comicContentService.getContentPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(comicContentService.getContentPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getContentPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            comicContentService.create(comic);
        }

        assertThat(
                comicContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                comicContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getById() {
        Comic saved = comicRepository.save(comic);

        assertThat(comicContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(c -> {
                    assertThat(c.getTitle()).isEqualTo(comic.getTitle());
                    assertThat(c.getChaptersCount()).isEqualTo(comic.getChaptersCount());
                });
    }

    @Test
    void create() {
        Comic saved = comicContentService.create(comic);

        assertThat(comicContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(c -> {
                    assertThat(c.getTitle()).isEqualTo(comic.getTitle());
                    assertThat(c.getChaptersCount()).isEqualTo(comic.getChaptersCount());
                });
    }

    @Test
    void update() {
        Comic saved = comicContentService.create(comic);

        saved.setTitle("Changed");
        saved.setNew(false);

        comicContentService.update(saved);

        assertThat(comicContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(c -> {
                    assertThat(c.getTitle()).isEqualTo(comic.getTitle());
                    assertThat(c.getChaptersCount()).isEqualTo(comic.getChaptersCount());
                });
    }

    @Test
    void delete() {
        Comic saved = comicContentService.create(comic);

        comicContentService.delete(saved.getId());

        assertThat(comicContentService.getById(saved.getId())).isNull();
    }

    @Test
    void testDelete() {
        Comic saved = comicContentService.create(comic);

        comicContentService.delete(saved);

        assertThat(comicContentService.getById(saved.getId())).isNull();
    }

    @Test
    void count() {
        comicContentService.create(comic);
        assertThat(comicContentService.count()).isEqualTo(1);

        comicContentService.create(comic);
        assertThat(comicContentService.count()).isEqualTo(2);

        comicContentService.create(comic);
        comicContentService.create(comic);
        assertThat(comicContentService.count()).isEqualTo(4);
    }

    @Test
    void getCardPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            comicContentService.create(comic);
        }

        assertThat(comicContentService.getCardPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(comicContentService.getCardPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(comicContentService.getCardPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(comicContentService.getCardPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getCardPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            comicContentService.create(comic);
        }

        assertThat(
                comicContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                comicContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getCardById() {
        Comic saved = comicContentService.create(comic);

        assertThat(comicContentService.getCardById(saved.getId()))
                .isNotNull()
                .satisfies(c -> {
                    assertThat(c.getAvgRating()).isEqualTo(5.0);
                    assertThat(c.getContent().getTitle()).isEqualTo(comic.getTitle());
                    assertThat(c.getContent().getChaptersCount()).isEqualTo(comic.getChaptersCount());
                });
    }
}
