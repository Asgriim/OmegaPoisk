package org.omega.ratingservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.ratingservice.dto.RatingDTO;
import org.omega.ratingservice.entity.AvgRating;
import org.omega.ratingservice.entity.Rating;
import org.omega.ratingservice.repository.AvgRatingRepository;
import org.omega.ratingservice.repository.RatingRepository;
import org.omega.ratingservice.service.AvgRatingService;
import org.omega.ratingservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatingController.class)
@Testcontainers
class RatingControllerTest {
    @MockBean
    private RatingService ratingService;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private AvgRatingRepository avgRatingRepository;

    @MockBean
    private AvgRatingService avgRatingService;

    static Rating rating;

    @MockBean
    KafkaProducerService kafkaProducerService;

    @Autowired
    MockMvc mvc;

    @Value("${spring.application.page}")
    int pageSize;

    @Value("${rating-service.token}")
    String token;

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

        RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                        RestAssured.config().getObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> objectMapper)
                );
    }

    @BeforeEach
    void beforeEach() {

        AvgRating avgRating = new AvgRating();
        avgRating.setContentId(1);
        avgRating.setAvgRate(4.0);

        Mockito.doNothing().when(kafkaProducerService).sendMessage(Mockito.anyString());
        Mockito.when(avgRatingService.getAvgRatingByContentId(Mockito.anyLong()))
                .thenReturn(avgRating);


        Rating mockRating = new Rating(3, 4, 2 , 1);
        Mockito.when(ratingService.create(Mockito.any()))
                .thenReturn(mockRating);

        Mockito.when(ratingService.update(Mockito.any()))
                .thenReturn(new Rating(3, 5, 2 , 1));

        Mockito.when(ratingService.findByContentIdAndUserId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockRating);

//        RestAssured.baseURI = "http://localhost:" + port;

        rating = new Rating(2, 4, 2 , 1);
        ratingRepository.deleteAll();

//        Rating saved = new Rating(3, 4, 2 , 1);
//        Mockito.when(ratingService.create(Mockito.any(Rating.class))).thenReturn(saved);
    }

    @Test
    void getAvgById() throws Exception {
        ratingService.create(rating);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/rating/{id}/avg", rating.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value((float) rating.getValue()));
    }

    @Test
    void createRating() throws Exception {
        RatingDTO ratingDTO = new RatingDTO(rating);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/content/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isForbidden());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/content/rating")
                        .header("Authorization", "Bearer " + token.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/rating/{id}/avg", rating.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value((float) rating.getValue()));
    }


    @Test
    void updateRating() throws Exception {
        Rating saved = ratingService.create(rating);
        saved.setValue(5);

        AvgRating avgRating = new AvgRating();
        avgRating.setAvgRate(5);
        Mockito.when(avgRatingService.getAvgRatingByContentId(Mockito.anyLong()))
                .thenReturn(avgRating);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/content/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new RatingDTO(saved))))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/rating/{id}/avg", saved.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(saved.getValue()));
    }


    @Test
    void delete() throws Exception {
        Rating saved = ratingService.create(rating);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/rating/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        AvgRating avgRating = new AvgRating();
        avgRating.setAvgRate(0);
        Mockito.when(avgRatingService.getAvgRatingByContentId(Mockito.anyLong()))
                .thenReturn(avgRating);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/rating/{id}/avg", rating.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(0f));
    }


}