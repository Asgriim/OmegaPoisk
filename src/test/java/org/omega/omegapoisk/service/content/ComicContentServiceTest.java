//package org.omega.omegapoisk.service.content;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.omega.omegapoisk.content.dto.ContentCardDTO;
//import org.omega.omegapoisk.content.entity.Comic;
//import org.omega.omegapoisk.content.repository.ComicRepository;
//import org.omega.omegapoisk.content.service.ComicContentService;
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
//class ComicContentServiceTest {
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    @Autowired
//    ComicContentService comicContentService;
//
//    @Autowired
//    ComicRepository comicRepository;
//
//    @Value("${spring.application.page-size}")
//    int pageSize;
//
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
//        comicRepository.deleteAll();
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
//    void shouldGetAllComic() {
//        comicContentService.create(new Comic(true, 13));
//        assertThat(comicContentService.countAll()).isEqualTo(1);
//
//        comicContentService.create(new Comic(false, 24));
//        assertThat(comicContentService.countAll()).isEqualTo(2);
//
//        comicContentService.create(new Comic(true, 17));
//        comicContentService.create(new Comic(true, 17));
//        assertThat(comicContentService.countAll()).isEqualTo(4);
//
//    }
//
//    @Test
//    void shouldGetComicById() {
//        Comic createdComic = comicContentService.create(new Comic(true, 13));
//        Comic retrievedComic = comicContentService.getById(createdComic.getId());
//
//        assertThat(retrievedComic).isNotNull();
//        assertThat(retrievedComic.getId()).isEqualTo(createdComic.getId());
//        assertThat(retrievedComic.getChaptersCount()).isEqualTo(createdComic.getChaptersCount());
//    }
//
//    @Test
//    void shouldUpdateComic() {
//        Comic createdComic = comicContentService.create(new Comic(true, 13));
//
//        createdComic.setChaptersCount(15);
//        Comic retrievedComic = comicContentService.update(createdComic);
//
//        assertThat(retrievedComic).isNotNull();
//        assertThat(retrievedComic.getChaptersCount()).isEqualTo(createdComic.getChaptersCount());
//    }
//
//    @Test
//    void shouldDeleteComic() {
//        Comic createdComic = comicContentService.create(new Comic(true, 13));
//
//        comicContentService.delete(createdComic.getId());
//
//        Comic retrievedComic = comicContentService.getById(createdComic.getId());
//
//        assertThat(retrievedComic).isNull();
//        assertThat(comicContentService.countAll()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldGetContentCardById() {
//        Comic createdComic = comicContentService.create(new Comic(true, 13));
//
//        ContentCardDTO<Comic> cardDTO = comicContentService.getContentCardById(createdComic.getId());
//
//        assertThat(cardDTO).isNotNull();
//        assertThat(cardDTO.getContent().getChaptersCount()).isEqualTo(createdComic.getChaptersCount());
//    }
//
//    @Test
//    void shouldGetComicPage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            comicContentService.create(new Comic(true, 10+i));
//        }
//
//        assertThat(comicContentService.getComicPage(0)).hasSize(5);
//        assertThat(comicContentService.getComicPage(1)).hasSize(5);
//        assertThat(comicContentService.getComicPage(2)).hasSize(2);
//        assertThat(comicContentService.getComicPage(3)).hasSize(0);
//
//    }
//
//    @Test
//    void shouldGetCardsPage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            comicContentService.create(new Comic(true, 10+i));
//        }
//
//        assertThat(comicContentService.getCardsPage(0)).hasSize(5);
//        assertThat(comicContentService.getCardsPage(1)).hasSize(5);
//        assertThat(comicContentService.getCardsPage(2)).hasSize(2);
//        assertThat(comicContentService.getCardsPage(3)).hasSize(0);
//
//    }
//
//
//}