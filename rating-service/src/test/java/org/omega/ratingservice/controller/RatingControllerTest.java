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
import org.omega.ratingservice.dto.RatingDTO;
import org.omega.ratingservice.entity.Rating;
import org.omega.ratingservice.repository.RatingRepository;
import org.omega.ratingservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RatingControllerTest {
    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    static Rating rating;

    @Value("${spring.application.page}")
    int pageSize;

    @Value("${rating-service.token}")
    String token;

    @LocalServerPort
    private Integer port;

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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                        RestAssured.config().getObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> objectMapper)
                );
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;

        rating = new Rating(0, 4, 2 , 1);
        ratingRepository.deleteAll();
    }

    @Test
    void getAvgById() {
        ratingService.create(rating);

        given()
                .pathParam("id", rating.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/rating/{id}/avg")
                .then()
                .statusCode(200)
                .body("value", equalTo((float)rating.getValue()));
    }

    @Test
    void createRating() {
        given()
                .contentType(ContentType.JSON)
                .body(new RatingDTO(rating))
                .when()
                .post("/api/v1/content/rating")
                .then()
                .statusCode(403);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new RatingDTO(rating))
                .when()
                .post("/api/v1/content/rating")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", rating.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/rating/{id}/avg")
                .then()
                .statusCode(200)
                .body("value", equalTo((float)rating.getValue()));
    }

    @Test
    void updateRating() {
        Rating saved = ratingService.create(rating);

        saved.setValue(5);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new RatingDTO(saved))
                .when()
                .put("/api/v1/content/rating")
                .then()
                .statusCode(201);

        given()
                .pathParam("id", rating.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/rating/{id}/avg")
                .then()
                .statusCode(200)
                .body("value", equalTo((float) 5));
    }

    @Test
    void delete() {
        Rating saved = ratingService.create(rating);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/content/rating/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", rating.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/rating/{id}/avg")
                .then()
                .statusCode(200)
                .body("value", equalTo((float) 0));
    }

}