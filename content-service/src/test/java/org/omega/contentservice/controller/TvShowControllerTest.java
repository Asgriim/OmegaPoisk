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
import org.omega.contentservice.dto.TvShowDTO;
import org.omega.contentservice.entity.TvShow;
import org.omega.contentservice.repository.*;
import org.omega.contentservice.service.TvShowContentService;
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

@WebMvcTest(TvShowController.class)
@MockBean(AnimeController.class)
@MockBean(ComicController.class)
@MockBean(GameController.class)
@MockBean(MovieController.class)
@MockBean(AnimeRepository.class)
@MockBean(ComicRepository.class)
@MockBean(GameRepository.class)
@MockBean(MovieRepository.class)
@MockBean(AvgRatingFeignClient.class)
@MockBean(TokenService.class)
@Testcontainers
class TvShowControllerTest {

    @MockBean
    private AvgRatingService avgRatingService;

    @MockBean
    private TvShowContentService tvShowContentService;

    @MockBean
    private TvShowRepository tvShowRepository;

    @MockBean
    KafkaProducerService kafkaProducerService;

    static TvShow tvShow;

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
        tvShow = new TvShow(120);
        tvShow.setId(1L);
        tvShow.setTitle("Test content");
        tvShow.setDescription("Test desc");
        tvShow.setNew(true);

        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);

        Mockito.when(tvShowContentService.getPage())
                .thenReturn(longPageSize);
        Mockito.when(tvShowContentService.getById(Mockito.any()))
                .thenReturn(tvShow);
        Mockito.when(tvShowContentService.getContentPageRange(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(tvShow));
        Mockito.when(tvShowContentService.getContentPage(Mockito.any()))
                .thenReturn(List.of(tvShow));
        Mockito.when(tvShowContentService.create(Mockito.any()))
                .thenReturn(tvShow);
        Mockito.when(tvShowContentService.update(Mockito.any()))
                .thenReturn(tvShow);

        Mockito.when(tvShowRepository.save(Mockito.any()))
                .thenReturn(tvShow);
        Mockito.when(tvShowRepository.getNextContentId())
                .thenReturn(1L);

        tvShowRepository.deleteAll();
    }

    @Test
    void getContentRange() throws Exception {
        tvShowContentService.create(tvShow);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show?page-to=1")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show?page=0")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById() throws Exception {
        TvShow saved = tvShowContentService.create(tvShow);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(tvShow.getTitle()));
    }

    @Test
    void create() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/content/tv-show")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new TvShowDTO(tvShow))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show/{id}", tvShow.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(tvShow.getTitle()));
    }

    @Test
    void update() throws Exception {
        TvShow saved = tvShowContentService.create(tvShow);
        saved.setTitle("Changed");
        saved.setNew(false);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/content/tv-show")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new TvShowDTO(tvShow))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Changed"));
    }

    @Test
    void delete() throws Exception {
        TvShow saved = tvShowContentService.create(tvShow);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/tv-show/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        Mockito.when(tvShowContentService.getById(Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/tv-show/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNotFound());
    }
}
