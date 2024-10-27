package org.omega.omegapoisk.controller.studio;

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
import org.omega.omegapoisk.studio.dto.StudioDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.studio.entity.Studio;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.studio.repository.StudioRepository;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudioControllerTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    StudioService studioService;

    @Autowired
    StudioRepository studioRepository;

    @Autowired
    AnimeContentService animeContentService;

    @Autowired
    AnimeRepository animeRepository;

    @Value("${spring.application.page-size}")
    int pageSize;

    static Anime anime;

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

        animeRepository.deleteAll();
        studioRepository.deleteAll();

        anime = animeContentService.create(new Anime(13));
        anime.setTitle("86");
        anime.setDescription("Robots.txt");

    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldGetById() {
        String name = "Best studio";
        studioService.createStudio(new Studio(0L, name));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body("name", contains(name));
    }

    @Test
    void shouldCreate() {
        studioRepository.deleteAll();

        String name = "Best studio";
        Studio studio = new Studio(0L, name);

        given()
                .contentType(ContentType.JSON)
                .body(new StudioDTO(studio))
                .when()
                .post("/api/v1/studio")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body("name", contains(name));
    }

    @Test
    void shouldGetAll() {
        studioRepository.deleteAll();

        studioService.createStudio(new Studio(0L, "Test"));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));

        studioService.createStudio(new Studio(0L, "Test"));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

        studioService.createStudio(new Studio(0L, "Test"));
        studioService.createStudio(new Studio(0L, "Test"));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(4));

    }

    @Test
    void shouldDelete() {
        studioRepository.deleteAll();

        String name = "Best studio";
        Studio studio = new Studio(0L, name);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new StudioDTO(studio))
                .when()
                .post("/api/v1/studio")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .when()
                .delete("/api/v1/studio/{id}")
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }
}