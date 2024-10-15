package org.omega.omegapoisk.controller.content;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.dto.content.GameDTO;
import org.omega.omegapoisk.entity.content.Game;
import org.omega.omegapoisk.repository.content.GameRepository;
import org.omega.omegapoisk.service.content.GameContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    GameContentService gameContentService;

    @Autowired
    GameRepository gameRepository;

    @Value("${spring.application.page-size}")
    int pageSize;

    static Game game;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                        RestAssured.config().getObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> objectMapper)
                );
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;

        gameRepository.deleteAll();
        game = gameContentService.create(new Game(false));
        game.setTitle("86");
        game.setDescription("Robots.txt");

    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/" + game.getId())
                .then()
                .statusCode(200)
                .body("free", equalTo(game.isFree()));
    }

    @Test
    void shouldGetCardById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/" + game.getId() + "/card")
                .then()
                .statusCode(200)
                .body("content.free", equalTo(game.isFree()));
    }

    @Test
    void shouldGetPage() {
        gameRepository.deleteAll();
        for (int i = 0; i < pageSize*2+2; i++) {
            gameContentService.create(new Game(false));
        }

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void shouldGetCardPage() {
        gameRepository.deleteAll();
        for (int i = 0; i < pageSize*2+2; i++) {
            gameContentService.create(new Game(false));
        }

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/card?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/card?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/card?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/game/card?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void shouldCreate() {
        gameRepository.deleteAll();

        String title = "86 2";
        String desc = "Robots.txt 2";

        Game game1 = new Game(false);
        game1.setId(1L);
        game1.setTitle(title);
        game1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new GameDTO(game1))
                .when()
                .post("/api/v1/content/game")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(response.path("id")));
    }

    @Test
    void shouldUpdate() {
        gameRepository.deleteAll();

        String title = "86 2";
        String desc = "Robots.txt 2";
        String changed = "Changed";

        Game game1 = new Game(false);
        game1.setId(1L);
        game1.setTitle(title);
        game1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new GameDTO(game1))
                .when()
                .post("/api/v1/content/game")
                .then()
                .statusCode(201)
                .extract().response();

        Integer extractedId = response.path("id");
        game1.setId(extractedId.longValue());
        game1.setTitle(changed);

        given()
                .contentType(ContentType.JSON)
                .body(new GameDTO(game1))
                .when()
                .put("/api/v1/content/game")
                .then()
                .statusCode(201);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(response.path("id")))
                .body("title", equalTo(changed));
    }

    @Test
    void shouldDelete() {
        gameRepository.deleteAll();

        String title = "86 2";
        String desc = "Robots.txt 2";

        Game game1 = new Game(false);
        game1.setId(1L);
        game1.setTitle(title);
        game1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new GameDTO(game1))
                .when()
                .post("/api/v1/content/game")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .when()
                .delete("/api/v1/content/game/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/game/{id}")
                .then()
                .statusCode(404);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/game/{id}/card")
                .then()
                .statusCode(404);
    }

}