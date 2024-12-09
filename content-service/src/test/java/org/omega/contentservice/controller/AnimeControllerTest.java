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
import org.omega.contentservice.dto.AnimeDTO;
import org.omega.contentservice.entity.Anime;
import org.omega.contentservice.repository.AnimeRepository;
import org.omega.contentservice.service.AnimeContentService;
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
class AnimeControllerTest {

    @Mock
    AvgRatingService avgRatingService;

    @Autowired
    private AnimeContentService animeContentService;

    @Autowired
    private AnimeRepository animeRepository;

    static Anime anime;

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
        animeContentService = new AnimeContentService(animeRepository, avgRatingService);

        RestAssured.baseURI = "http://localhost:" + port;

        anime = new Anime(220);
        anime.setId(0L);
        anime.setTitle("Test content");
        anime.setDescription("Test desc");
        anime.setNew(true);
        animeRepository.deleteAll();
    }

    @Test
    void getContentRange() {
        for (int i = 0; i < pageSize*2+2; i++) {
            animeContentService.create(anime);
        }

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime?page-to=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize*2));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

        given()
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void getById() {
        Anime saved = animeContentService.create(anime);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo(anime.getTitle()));
    }

    @Test
    void create() {
        given()
                .contentType(ContentType.JSON)
                .body(new AnimeDTO(anime))
                .when()
                .post("/api/v1/content/anime")
                .then()
                .statusCode(403);

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new AnimeDTO(anime))
                .when()
                .post("/api/v1/content/anime")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo(anime.getTitle()));
    }

    @Test
    void update() {
        Anime saved = animeContentService.create(anime);
        saved.setTitle("Changed");

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new AnimeDTO(saved))
                .when()
                .put("/api/v1/content/anime")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime/{id}")
                .then()
                .statusCode(200)
                .body("title", equalTo("Changed"));

    }

    @Test
    void delete() {
        Anime saved = animeContentService.create(anime);

        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/content/anime/{id}")
                .then()
                .statusCode(204);


        given()
                .pathParam("id", saved.getId())
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/content/anime/{id}")
                .then()
                .statusCode(404);
    }
}