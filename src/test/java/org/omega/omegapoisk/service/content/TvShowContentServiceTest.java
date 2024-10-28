//package org.omega.omegapoisk.service.content;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.omega.omegapoisk.content.dto.ContentCardDTO;
//import org.omega.omegapoisk.content.entity.TvShow;
//import org.omega.omegapoisk.content.repository.TvShowRepository;
//import org.omega.omegapoisk.content.service.TvShowContentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Testcontainers
//class TvShowContentServiceTest {
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    @Autowired
//    TvShowContentService tvShowContentService;
//
//    @Autowired
//    TvShowRepository tvShowRepository;
//
//    @Value("${spring.application.page-size}")
//    int pageSize;
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }
//
//    @BeforeEach
//    void setUp() {
//        tvShowRepository.deleteAll();
//    }
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    @Test
//    void shouldGetAllTvShow() {
//        tvShowContentService.create(new TvShow(13));
//        assertThat(tvShowContentService.countAll()).isEqualTo(1);
//
//        tvShowContentService.create(new TvShow(15));
//        assertThat(tvShowContentService.countAll()).isEqualTo(2);
//
//        tvShowContentService.create(new TvShow(12));
//        tvShowContentService.create(new TvShow(12));
//        assertThat(tvShowContentService.countAll()).isEqualTo(4);
//
//    }
//
//    @Test
//    void shouldGetTvShowById() {
//        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));
//        TvShow retrievedTvShow = tvShowContentService.getById(createdTvShow.getId());
//
//        assertThat(retrievedTvShow).isNotNull();
//        assertThat(retrievedTvShow.getId()).isEqualTo(createdTvShow.getId());
//        assertThat(retrievedTvShow.getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
//    }
//
//    @Test
//    void shouldUpdateTvShow() {
//        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));
//
//        createdTvShow.setSeriesNum(15);
//        TvShow retrievedTvShow = tvShowContentService.update(createdTvShow);
//
//        assertThat(retrievedTvShow).isNotNull();
//        assertThat(retrievedTvShow.getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
//    }
//
//    @Test
//    void shouldDeleteTvShow() {
//        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));
//
//        tvShowContentService.delete(createdTvShow.getId());
//
//        TvShow retrievedTvShow = tvShowContentService.getById(createdTvShow.getId());
//
//        assertThat(retrievedTvShow).isNull();
//        assertThat(tvShowContentService.countAll()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldGetContentCardById() {
//        TvShow createdTvShow = tvShowContentService.create(new TvShow(13));
//
//        ContentCardDTO<TvShow> cardDTO = tvShowContentService.getContentCardById(createdTvShow.getId());
//
//        assertThat(cardDTO).isNotNull();
//        assertThat(cardDTO.getContent().getSeriesNum()).isEqualTo(createdTvShow.getSeriesNum());
//    }
//
//    @Test
//    void shouldGetTvShowPage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            tvShowContentService.create(new TvShow(10+i));
//        }
//
//        assertThat(tvShowContentService.getTvShowPage(0)).hasSize(5);
//        assertThat(tvShowContentService.getTvShowPage(1)).hasSize(5);
//        assertThat(tvShowContentService.getTvShowPage(2)).hasSize(2);
//        assertThat(tvShowContentService.getTvShowPage(3)).hasSize(0);
//
//    }
//
//    @Test
//    void shouldGetCardsPage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            tvShowContentService.create(new TvShow(10+i));
//        }
//
//        assertThat(tvShowContentService.getCardsPage(0)).hasSize(5);
//        assertThat(tvShowContentService.getCardsPage(1)).hasSize(5);
//        assertThat(tvShowContentService.getCardsPage(2)).hasSize(2);
//        assertThat(tvShowContentService.getCardsPage(3)).hasSize(0);
//
//    }
//
//
//}