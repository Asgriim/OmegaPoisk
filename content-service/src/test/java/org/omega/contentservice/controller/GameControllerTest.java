package org.omega.contentservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.client.AvgRatingFeignClient;
import org.omega.contentservice.dto.GameDTO;
import org.omega.contentservice.entity.Game;
import org.omega.contentservice.repository.*;
import org.omega.contentservice.service.GameContentService;
import org.omega.contentservice.service.AvgRatingService;
import org.omega.contentservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@MockBean(AnimeController.class)
@MockBean(ComicController.class)
@MockBean(MovieController.class)
@MockBean(TvShowController.class)
@MockBean(AnimeRepository.class)
@MockBean(ComicRepository.class)
@MockBean(MovieRepository.class)
@MockBean(TvShowRepository.class)
@MockBean(AvgRatingFeignClient.class)
@MockBean(TokenService.class)
@Testcontainers
class GameControllerTest {

    @MockBean
    private AvgRatingService avgRatingService;

    @MockBean
    private GameContentService gameContentService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    KafkaProducerService kafkaProducerService;

    static Game game;

    @Autowired
    private MockMvc mockMvc;

    @Value("${content-service.token}")
    String token;

    @Value("${spring.application.page}")
    long longPageSize;

    @Value("${spring.application.page}")
    int pageSize;

    static ObjectMapper objectMapper;

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

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @BeforeEach
    void beforeEach() {
        game = new Game(false);
        game.setId(1L);
        game.setTitle("Test content");
        game.setDescription("Test desc");
        game.setNew(true);

        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);

        Mockito.when(gameContentService.getPage())
                .thenReturn(longPageSize);
        Mockito.when(gameContentService.getById(Mockito.any()))
                .thenReturn(game);
        Mockito.when(gameContentService.getContentPageRange(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(game));
        Mockito.when(gameContentService.getContentPage(Mockito.any()))
                .thenReturn(List.of(game));
        Mockito.when(gameContentService.create(Mockito.any()))
                .thenReturn(game);
        Mockito.when(gameContentService.update(Mockito.any()))
                .thenReturn(game);

        Mockito.when(gameRepository.save(Mockito.any()))
                .thenReturn(game);
        Mockito.when(gameRepository.getNextContentId())
                .thenReturn(1L);

        gameRepository.deleteAll();
    }

    @Test
    void getContentRange() throws Exception {
        gameContentService.create(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game?page-to=1")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game?page=0")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById() throws Exception {
        Game saved = gameContentService.create(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(game.getTitle()));
    }

    @Test
    void create() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/content/game")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new GameDTO(game))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game/{id}", game.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(game.getTitle()));
    }

    @Test
    void update() throws Exception {
        Game saved = gameContentService.create(game);
        saved.setTitle("Changed");
        saved.setNew(false);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/content/game")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new GameDTO(game))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Changed"));
    }

    @Test
    void delete() throws Exception {
        Game saved = gameContentService.create(game);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/game/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        Mockito.when(gameContentService.getById(Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/game/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNotFound());
    }
}
