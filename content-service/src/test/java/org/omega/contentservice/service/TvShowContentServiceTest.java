package org.omega.contentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.contentservice.dto.MovieDTO;
import org.omega.contentservice.dto.TvShowDTO;
import org.omega.contentservice.entity.TvShow;
import org.omega.contentservice.repository.TvShowRepository;
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
class TvShowContentServiceTest {

    @Mock
    AvgRatingService avgRatingService;

    private TvShowContentService tvShowContentService;

    @Autowired
    private TvShowRepository tvShowRepository;

    static TvShow tvShow;

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
        tvShowContentService = new TvShowContentService(tvShowRepository, avgRatingService);

        tvShow = new TvShow(220);
        tvShow.setId(0L);
        tvShow.setTitle("Test content");
        tvShow.setDescription("Test desc");
        tvShow.setNew(true);
        tvShowRepository.deleteAll();
    }

    @Test
    void entityTest() {
        tvShow.setNew(false);
        assertThat(new TvShowDTO(tvShow).toEntity()).isEqualTo(tvShow);
    }

    @Test
    void getContentPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            tvShowContentService.create(tvShow);
        }

        assertThat(tvShowContentService.getContentPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getContentPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            tvShowContentService.create(tvShow);
        }

        assertThat(
                tvShowContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                tvShowContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getById() {
        TvShow saved = tvShowRepository.save(tvShow);

        assertThat(tvShowContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(t -> {
                    assertThat(t.getTitle()).isEqualTo(tvShow.getTitle());
                    assertThat(t.getSeriesNum()).isEqualTo(tvShow.getSeriesNum());
                });
    }

    @Test
    void create() {
        TvShow saved = tvShowContentService.create(tvShow);

        assertThat(tvShowContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(t -> {
                    assertThat(t.getTitle()).isEqualTo(tvShow.getTitle());
                    assertThat(t.getSeriesNum()).isEqualTo(tvShow.getSeriesNum());
                });
    }

    @Test
    void update() {
        TvShow saved = tvShowContentService.create(tvShow);

        saved.setTitle("Changed");
        saved.setNew(false);

        tvShowContentService.update(saved);

        assertThat(tvShowContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(t -> {
                    assertThat(t.getTitle()).isEqualTo(tvShow.getTitle());
                    assertThat(t.getSeriesNum()).isEqualTo(tvShow.getSeriesNum());
                });
    }

    @Test
    void delete() {
        TvShow saved = tvShowContentService.create(tvShow);

        tvShowContentService.delete(saved.getId());

        assertThat(tvShowContentService.getById(saved.getId())).isNull();
    }

    @Test
    void testDelete() {
        TvShow saved = tvShowContentService.create(tvShow);

        tvShowContentService.delete(saved);

        assertThat(tvShowContentService.getById(saved.getId())).isNull();
    }

    @Test
    void count() {
        tvShowContentService.create(tvShow);
        assertThat(tvShowContentService.count()).isEqualTo(1);

        tvShowContentService.create(tvShow);
        assertThat(tvShowContentService.count()).isEqualTo(2);

        tvShowContentService.create(tvShow);
        tvShowContentService.create(tvShow);
        assertThat(tvShowContentService.count()).isEqualTo(4);
    }

    @Test
    void getCardPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            tvShowContentService.create(tvShow);
        }

        assertThat(tvShowContentService.getCardPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getCardPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            tvShowContentService.create(tvShow);
        }

        assertThat(
                tvShowContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                tvShowContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getCardById() {
        TvShow saved = tvShowContentService.create(tvShow);

        assertThat(tvShowContentService.getCardById(saved.getId()))
                .isNotNull()
                .satisfies(t -> {
                    assertThat(t.getAvgRating()).isEqualTo(5.0);
                    assertThat(t.getContent().getTitle()).isEqualTo(tvShow.getTitle());
                    assertThat(t.getContent().getSeriesNum()).isEqualTo(tvShow.getSeriesNum());
                });
    }
}
