package org.omega.omegapoisk.content.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.ContentCard;
import org.omega.omegapoisk.content.entity.TvShow;
import org.omega.omegapoisk.content.repository.TvShowRepository;
import org.omega.omegapoisk.content.service.TvShowContentService;
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
class TvShowContentServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    TvShowContentService tvShowContentService;

    @Autowired
    TvShowRepository tvShowRepository;

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
        tvShowRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetAllTvShow() {
        tvShowContentService.create(new TvShow(13));
        assertThat(tvShowContentService.count()).isEqualTo(1);

        tvShowContentService.create(new TvShow(15));
        assertThat(tvShowContentService.count()).isEqualTo(2);

        tvShowContentService.create(new TvShow(12));
        tvShowContentService.create(new TvShow(12));
        assertThat(tvShowContentService.count()).isEqualTo(4);

    }

    @Test
    void shouldGetTvShowById() {
        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));
        TvShow retrievedTvShow = tvShowContentService.getById(createdTvShow.getId());

        assertThat(retrievedTvShow).isNotNull();
        assertThat(retrievedTvShow.getId()).isEqualTo(createdTvShow.getId());
        assertThat(retrievedTvShow.getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
    }

    @Test
    void shouldUpdateTvShow() {
        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));

        createdTvShow.setSeriesNum(15);
        TvShow retrievedTvShow = tvShowContentService.update(createdTvShow);

        assertThat(retrievedTvShow).isNotNull();
        assertThat(retrievedTvShow.getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
    }

    @Test
    void shouldDeleteTvShow() {
        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));

        tvShowContentService.delete(createdTvShow.getId());

        TvShow retrievedTvShow = tvShowContentService.getById(createdTvShow.getId());

        assertThat(retrievedTvShow).isNull();
        assertThat(tvShowContentService.count()).isEqualTo(0);
    }

    @Test
    void shouldGetContentCardById() {
        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));

        ContentCard<TvShow> card = tvShowContentService.getCardById(createdTvShow.getId());

        assertThat(card).isNotNull();
        assertThat(card.getContent().getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
    }

    @Test
    void shouldGetTvShowPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            tvShowContentService.create(new TvShow(10+i));
        }

        assertThat(tvShowContentService.getContentPage(PageRequest.of(0, pageSize))).hasSize(5);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(1, pageSize))).hasSize(5);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(2, pageSize))).hasSize(2);
        assertThat(tvShowContentService.getContentPage(PageRequest.of(3, pageSize))).hasSize(0);

    }

    @Test
    void shouldGetCardsPage() {
        for (int i = 0; i < pageSize*2+2; i++) {
            tvShowContentService.create(new TvShow(10+i));
        }

        assertThat(tvShowContentService.getCardPage(PageRequest.of(0, pageSize))).hasSize(5);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(1, pageSize))).hasSize(5);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(2, pageSize))).hasSize(2);
        assertThat(tvShowContentService.getCardPage(PageRequest.of(3, pageSize))).hasSize(0);

    }


}