//package org.omega.omegapoisk.service.content;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.omega.omegapoisk.content.dto.ContentCardDTO;
//import org.omega.omegapoisk.content.entity.Game;
//import org.omega.omegapoisk.content.repository.GameRepository;
//import org.omega.omegapoisk.content.service.GameContentService;
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
//class GameContentServiceTest {
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    @Autowired
//    GameContentService gameContentService;
//
//    @Autowired
//    GameRepository gameRepository;
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
//        gameRepository.deleteAll();
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
//    void shouldGetAllGame() {
//        gameContentService.create(new Game(true));
//        assertThat(gameContentService.countAll()).isEqualTo(1);
//
//        gameContentService.create(new Game(true));
//        assertThat(gameContentService.countAll()).isEqualTo(2);
//
//        gameContentService.create(new Game(true));
//        gameContentService.create(new Game(true));
//        assertThat(gameContentService.countAll()).isEqualTo(4);
//
//    }
//
//    @Test
//    void shouldGetGameById() {
//        Game createdGame = gameContentService.create(new Game(true));
//        Game retrievedGame = gameContentService.getById(createdGame.getId());
//
//        assertThat(retrievedGame).isNotNull();
//        assertThat(retrievedGame.getId()).isEqualTo(createdGame.getId());
//        assertThat(retrievedGame.isFree()).isEqualTo(createdGame.isFree());
//    }
//
//    @Test
//    void shouldUpdateGame() {
//        Game createdGame = gameContentService.create(new Game(true));
//
//        createdGame.setFree(false);
//        Game retrievedGame = gameContentService.update(createdGame);
//
//        assertThat(retrievedGame).isNotNull();
//        assertThat(retrievedGame.isFree()).isEqualTo(createdGame.isFree());
//    }
//
//    @Test
//    void shouldDeleteGame() {
//        Game createdGame = gameContentService.create(new Game(true));
//
//        gameContentService.delete(createdGame.getId());
//
//        Game retrievedGame = gameContentService.getById(createdGame.getId());
//
//        assertThat(retrievedGame).isNull();
//        assertThat(gameContentService.countAll()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldGetContentCardById() {
//        Game createdGame = gameContentService.create(new Game(true));
//
//        ContentCardDTO<Game> cardDTO = gameContentService.getContentCardById(createdGame.getId());
//
//        assertThat(cardDTO).isNotNull();
//        assertThat(cardDTO.getContent().isFree()).isEqualTo(createdGame.isFree());
//    }
//
//    @Test
//    void shouldGetGamePage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            gameContentService.create(new Game(true));
//        }
//
//        assertThat(gameContentService.getGamePage(0)).hasSize(5);
//        assertThat(gameContentService.getGamePage(1)).hasSize(5);
//        assertThat(gameContentService.getGamePage(2)).hasSize(2);
//        assertThat(gameContentService.getGamePage(3)).hasSize(0);
//
//    }
//
//    @Test
//    void shouldGetCardsPage() {
//        for (int i = 0; i < pageSize*2+2; i++) {
//            gameContentService.create(new Game(true));
//        }
//
//        assertThat(gameContentService.getCardsPage(0)).hasSize(5);
//        assertThat(gameContentService.getCardsPage(1)).hasSize(5);
//        assertThat(gameContentService.getCardsPage(2)).hasSize(2);
//        assertThat(gameContentService.getCardsPage(3)).hasSize(0);
//
//    }
//
//
//}