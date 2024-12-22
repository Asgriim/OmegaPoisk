package org.omega.reviewservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.reviewservice.dto.ReviewDTO;
import org.omega.reviewservice.entity.Review;
import org.omega.reviewservice.repository.ReviewRepository;
import org.omega.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@Testcontainers
class ReviewControllerTest {

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    KafkaProducerService kafkaProducerService;

    static Review review;

    @Autowired
    MockMvc mvc;

    @Value("${spring.application.page}")
    int pageSize;

    @Value("${review-service.token}")
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
    }

    @BeforeEach
    void beforeEach() {
        review = new Review(0, "Imba title!", 2 , 1);

        Mockito.doNothing().when(kafkaProducerService).sendMessage(Mockito.anyString());
        Mockito.when(reviewService.getPage())
                .thenReturn(pageSize);
        Mockito.when(reviewService.create(Mockito.any()))
                .thenReturn(review);
        Mockito.when(reviewService.update(Mockito.any()))
                .thenReturn(new Review(1, "Updated review", 2 , 1));
        Mockito.when(reviewService.getPageByContentId(Mockito.any(), Mockito.anyLong()))
                .thenReturn(List.of(review));

        reviewRepository.deleteAll();
    }

    @Test
    void getByContentId() throws Exception {
        reviewService.create(review);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/review/{id}", review.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].txt").value(review.getTxt()));
    }

    @Test
    void createReview() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO(review);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/content/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isForbidden());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/content/review")
                        .header("Authorization", "Bearer " + token.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/review/{id}", review.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].txt").value(review.getTxt()));
    }

    @Test
    void updateReview() throws Exception {
        Review saved = reviewService.create(new Review(0, "Imba 1", 2 , 1));
        saved.setTxt("Changed");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/content/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token.trim())
                        .content(objectMapper.writeValueAsString(new ReviewDTO(saved))))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/review/{id}", saved.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].txt").value("Changed"));
    }

    @Test
    void deleteReview() throws Exception {
        Review saved = reviewService.create(new Review(0, "Imba 1", 2 , 1));
        Mockito.when(reviewService.getPageByContentId(Mockito.any(), Mockito.anyLong()))
                .thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/content/review/{id}", saved.getId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isNoContent());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/content/review/{id}", saved.getContentId())
                        .header("Authorization", "Bearer " + token.trim()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
