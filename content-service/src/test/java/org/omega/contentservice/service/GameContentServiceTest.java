package org.omega.contentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.controller.AnimeController;
import org.omega.contentservice.controller.GameController;
import org.omega.contentservice.dto.ComicDTO;
import org.omega.contentservice.dto.GameDTO;
import org.omega.contentservice.entity.Game;
import org.omega.contentservice.repository.GameRepository;
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
@MockBean(GameController.class)
@MockBean(KafkaProducerService.class)
@Testcontainers
class GameContentServiceTest {

    @Mock
    AvgRatingService avgRatingService;

    private GameContentService gameContentService;

    @Autowired
    private GameRepository gameRepository;

    static Game game;

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
        gameContentService = new GameContentService(avgRatingService, gameRepository);

        game = new Game(false);
        game.setId(0L);
        game.setTitle("Test content");
        game.setDescription("Test desc");
        game.setNew(true);
        gameRepository.deleteAll();
    }

    @Test
    void entityTest() {
        game.setNew(false);
        assertThat(new GameDTO(game).toEntity()).isEqualTo(game);
    }

    @Test
    void getContentPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            gameContentService.create(game);
        }

        assertThat(gameContentService.getContentPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(gameContentService.getContentPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(gameContentService.getContentPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(gameContentService.getContentPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getContentPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            gameContentService.create(game);
        }

        assertThat(
                gameContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                gameContentService.getContentPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getById() {
        Game saved = gameRepository.save(game);

        assertThat(gameContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getTitle()).isEqualTo(game.getTitle());
                    assertThat(g.isFree()).isEqualTo(game.isFree());
                });
    }

    @Test
    void create() {
        Game saved = gameContentService.create(game);

        assertThat(gameContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getTitle()).isEqualTo(game.getTitle());
                    assertThat(g.isFree()).isEqualTo(game.isFree());
                });
    }

    @Test
    void update() {
        Game saved = gameContentService.create(game);

        saved.setTitle("Changed");
        saved.setNew(false);

        gameContentService.update(saved);

        assertThat(gameContentService.getById(saved.getId()))
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getTitle()).isEqualTo(game.getTitle());
                    assertThat(g.isFree()).isEqualTo(game.isFree());
                });
    }

    @Test
    void delete() {
        Game saved = gameContentService.create(game);

        gameContentService.delete(saved.getId());

        assertThat(gameContentService.getById(saved.getId())).isNull();
    }

    @Test
    void testDelete() {
        Game saved = gameContentService.create(game);

        gameContentService.delete(saved);

        assertThat(gameContentService.getById(saved.getId())).isNull();
    }

    @Test
    void count() {
        gameContentService.create(game);
        assertThat(gameContentService.count()).isEqualTo(1);

        gameContentService.create(game);
        assertThat(gameContentService.count()).isEqualTo(2);

        gameContentService.create(game);
        gameContentService.create(game);
        assertThat(gameContentService.count()).isEqualTo(4);
    }

    @Test
    void getCardPage() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            gameContentService.create(game);
        }

        assertThat(gameContentService.getCardPage(PageRequest.of(0, pageSize)))
                .hasSize(pageSize);
        assertThat(gameContentService.getCardPage(PageRequest.of(1, pageSize)))
                .hasSize(pageSize);
        assertThat(gameContentService.getCardPage(PageRequest.of(2, pageSize)))
                .hasSize(2);
        assertThat(gameContentService.getCardPage(PageRequest.of(3, pageSize)))
                .hasSize(0);
    }

    @Test
    void getCardPageRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            gameContentService.create(game);
        }

        assertThat(
                gameContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(1, pageSize)
                )
        ).hasSize(10);

        assertThat(
                gameContentService.getCardPageRange(
                        PageRequest.of(0, pageSize),
                        PageRequest.of(2, pageSize)
                )
        ).hasSize(12);
    }

    @Test
    void getCardById() {
        Game saved = gameContentService.create(game);

        assertThat(gameContentService.getCardById(saved.getId()))
                .isNotNull()
                .satisfies(g -> {
                    assertThat(g.getAvgRating()).isEqualTo(5.0);
                    assertThat(g.getContent().getTitle()).isEqualTo(game.getTitle());
                    assertThat(g.getContent().isFree()).isEqualTo(game.isFree());
                });
    }
}
