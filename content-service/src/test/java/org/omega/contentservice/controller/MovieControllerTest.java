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
import org.omega.contentservice.dto.MovieDTO;
import org.omega.contentservice.entity.Movie;
import org.omega.contentservice.repository.*;
import org.omega.contentservice.service.MovieContentService;
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

@WebMvcTest(MovieController.class)
@MockBean(AnimeController.class)
@MockBean(ComicController.class)
@MockBean(GameController.class)
@MockBean(TvShowController.class)
@MockBean(AnimeRepository.class)
@MockBean(ComicRepository.class)
@MockBean(GameRepository.class)
@MockBean(TvShowRepository.class)
@MockBean(AvgRatingFeignClient.class)
@MockBean(TokenService.class)
@Testcontainers
class MovieControllerTest {

    @MockBean
    private AvgRatingService avgRatingService;

    @MockBean
    private MovieContentService movieContentService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    KafkaProducerService kafkaProducerService;

    static Movie movie;

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
        movie = new Movie(120);
        movie.setId(1L);
        movie.setTitle("Test content");
        movie.setDescription("Test desc");
        movie.setNew(true);

        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);

        Mockito.when(movieContentService.getPage())
                .thenReturn(longPageSize);
        Mockito.when(movieContentService.getById(Mockito.any()))
                .thenReturn(movie);
        Mockito.when(movieContentService.getContentPageRange(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(movie));
        Mockito.when(movieContentService.getContentPage(Mockito.any()))
                .thenReturn(List.of(movie));
        Mockito.when(movieContentService.create(Mockito.any()))
                .thenReturn(movie);
        Mockito.when(movieContentService.update(Mockito.any()))
                .thenReturn(movie);

        Mockito.when(movieRepository.save(Mockito.any()))
                .thenReturn(movie);
        Mockito.when(movieRepository.getNextContentId())
                .thenReturn(1L);

        movieRepository.deleteAll();
    }

    @Test
    void getContentRange() throws Exception {
        movieContentService.create(movie);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie?page-to=1")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie?page=0")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById() throws Exception {
        Movie saved = movieContentService.create(movie);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()));
    }

    @Test
    void create() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/content/movie")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new MovieDTO(movie))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie/{id}", movie.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()));
    }

    @Test
    void update() throws Exception {
        Movie saved = movieContentService.create(movie);
        saved.setTitle("Changed");
        saved.setNew(false);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/content/movie")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new MovieDTO(movie))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Changed"));
    }

    @Test
    void delete() throws Exception {
        Movie saved = movieContentService.create(movie);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/movie/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        Mockito.when(movieContentService.getById(Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/movie/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNotFound());
    }
}
