package org.omega.contentservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.omega.contentservice.dto.GameDTO;
import org.omega.contentservice.entity.Game;
import org.omega.contentservice.repository.GameRepository;
import org.omega.contentservice.service.GameContentService;
import org.omega.contentservice.service.AvgRatingService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class GameControllerTest {

    @Mock
    AvgRatingService avgRatingService;

    @Autowired
    private GameContentService gameContentService;

    @Autowired
    private GameRepository gameRepository;

    static Game game;

    @Value("${spring.application.page}")
    int pageSize;

    @Value("${content-service.token}")
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
        when(avgRatingService.getAvgRatingByContentId(anyLong())).thenReturn(5.0);
        gameContentService = new GameContentService(avgRatingService, gameRepository);

        RestAssured.baseURI = "http://localhost:" + port;

        game = new Game(true);
        game.setId(0L);
        game.setTitle("Test content");
        game.setDescription("Test desc");
        game.setNew(true);
        gameRepository.deleteAll();
    }

    @Test
    void getContentRange() {
        for (int i = 0; i < pageSize * 2 + 2; i++) {
            gameContentService.create(game);
        }

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game?page-to=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize*2));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void getById() {
        Game saved = gameContentService.create(game);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo(game.getTitle()));
    }

    @Test
    void create() {
        given()
                .contentType(ContentType.JSON)
                .body(new GameDTO(game))
                .when()
                .post("/api/v1/content/game")
                .then()
                .statusCode(403);

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new GameDTO(game))
                .when()
                .post("/api/v1/content/game")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo(game.getTitle()));
    }

    @Test
    void update() {
        Game saved = gameContentService.create(game);
        saved.setTitle("Changed");

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new GameDTO(saved))
                .when()
                .put("/api/v1/content/game")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Changed"));

    }

    @Test
    void delete() {
        Game saved = gameContentService.create(game);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/content/game/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(404);
    }
}