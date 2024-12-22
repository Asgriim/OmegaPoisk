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
import org.omega.contentservice.dto.ComicDTO;
import org.omega.contentservice.entity.Comic;
import org.omega.contentservice.repository.*;
import org.omega.contentservice.service.ComicContentService;
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

@WebMvcTest(ComicController.class)
@MockBean(AnimeController.class)
@MockBean(GameController.class)
@MockBean(MovieController.class)
@MockBean(TvShowController.class)
@MockBean(AnimeRepository.class)
@MockBean(GameRepository.class)
@MockBean(MovieRepository.class)
@MockBean(TvShowRepository.class)
@MockBean(AvgRatingFeignClient.class)
@MockBean(TokenService.class)
@Testcontainers
class ComicControllerTest {

    @MockBean
    private AvgRatingService avgRatingService;

    @MockBean
    private ComicContentService comicContentService;

    @MockBean
    private ComicRepository comicRepository;

    @MockBean
    KafkaProducerService kafkaProducerService;

    static Comic comic;

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
        comic = new Comic(false, 220);
        comic.setId(1L);
        comic.setTitle("Test content");
        comic.setDescription("Test desc");
        comic.setNew(true);

        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);

        Mockito.when(comicContentService.getPage())
                .thenReturn(longPageSize);
        Mockito.when(comicContentService.getById(Mockito.any()))
                .thenReturn(comic);
        Mockito.when(comicContentService.getContentPageRange(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(comic));
        Mockito.when(comicContentService.getContentPage(Mockito.any()))
                .thenReturn(List.of(comic));
        Mockito.when(comicContentService.create(Mockito.any()))
                .thenReturn(comic);
        Mockito.when(comicContentService.update(Mockito.any()))
                .thenReturn(comic);

        Mockito.when(comicRepository.save(Mockito.any()))
                .thenReturn(comic);
        Mockito.when(comicRepository.getNextContentId())
                .thenReturn(1L);

        comicRepository.deleteAll();
    }

    @Test
    void getContentRange() throws Exception {
        comicContentService.create(comic);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic?page-to=1")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic?page=0")
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById() throws Exception {
        Comic saved = comicContentService.create(comic);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(comic.getTitle()));
    }

    @Test
    void create() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/content/comic")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new ComicDTO(comic))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic/{id}", comic.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(comic.getTitle()));
    }

    @Test
    void update() throws Exception {
        Comic saved = comicContentService.create(comic);
        saved.setTitle("Changed");
        saved.setNew(false);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/content/comic")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new ComicDTO(comic))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Changed"));
    }

    @Test
    void delete() throws Exception {
        Comic saved = comicContentService.create(comic);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/comic/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        Mockito.when(comicContentService.getById(Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/content/comic/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNotFound());
    }
}
