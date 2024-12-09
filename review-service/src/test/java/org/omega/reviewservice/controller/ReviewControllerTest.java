package org.omega.reviewservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.reviewservice.dto.ReviewDTO;
import org.omega.reviewservice.entity.Review;
import org.omega.reviewservice.repository.ReviewRepository;
import org.omega.reviewservice.service.ReviewService;
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
class ReviewControllerTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    static Review review;

    @Value("${spring.application.page}")
    int pageSize;

    @Value("${review-service.token}")
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

        review = new Review(0, "Imba title!", 2 , 1);
        reviewRepository.deleteAll();
    }

    @Test
    void getByContentId() {
        Review saved = reviewService.create(review);

        given()
                .pathParam("id", saved.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/review/{id}")
                .then()
                .statusCode(200)
                .body("txt", hasItem(review.getTxt()));

        given()
                .pathParam("id", saved.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/review/{id}?page=0")
                .then()
                .statusCode(200)
                .body("txt", hasItem(review.getTxt()));
    }

    @Test
    void createReview() {
        given()
                .contentType(ContentType.JSON)
                .body(new ReviewDTO(review))
                .when()
                .post("/api/v1/content/review")
                .then()
                .statusCode(403);

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new ReviewDTO(review))
                .when()
                .post("/api/v1/content/review")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("content_id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/review/{id}")
                .then()
                .statusCode(200)
                .body("txt", hasItem(review.getTxt()));
    }

    @Test
    void updateReview() {
        Review saved = reviewService.create(new Review(0, "Imba 1", 2 , 1));

        saved.setTxt("Changed");

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new ReviewDTO(saved))
                .when()
                .put("/api/v1/content/review")
                .then()
                .statusCode(201);

        given()
                .pathParam("id", saved.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/review/{id}")
                .then()
                .statusCode(200)
                .body("txt", hasItem("Changed"));
    }

    @Test
    void delete() {
        Review saved = reviewService.create(new Review(0, "Imba 1", 2 , 1));

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/content/review/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", saved.getContentId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/review/{id}")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }
}