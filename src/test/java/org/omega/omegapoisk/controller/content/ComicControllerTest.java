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
import org.omega.omegapoisk.dto.content.ComicDTO;
import org.omega.omegapoisk.entity.content.Comic;
import org.omega.omegapoisk.repository.content.ComicRepository;
import org.omega.omegapoisk.service.content.ComicContentService;
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
class ComicControllerTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    ComicContentService comicContentService;

    @Autowired
    ComicRepository comicRepository;

    @Value("${spring.application.page-size}")
    int pageSize;

    static Comic comic;

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

        comicRepository.deleteAll();
        comic = comicContentService.create(new Comic(true, 13));
        comic.setTitle("86");
        comic.setDescription("Robots.txt");

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
                .get("/api/v1/content/comic/" + comic.getId())
                .then()
                .statusCode(200)
                .body("chapters_count", equalTo(comic.getChaptersCount()));
    }

    @Test
    void shouldGetCardById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic/" + comic.getId() + "/card")
                .then()
                .statusCode(200)
                .body("content.chapters_count", equalTo(comic.getChaptersCount()));
    }

    @Test
    void shouldGetPage() {
        comicRepository.deleteAll();
        for (int i = 0; i < pageSize*2+2; i++) {
            comicContentService.create(new Comic(true, 13));
        }

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void shouldGetCardPage() {
        comicRepository.deleteAll();
        for (int i = 0; i < pageSize*2+2; i++) {
            comicContentService.create(new Comic(true, 13));
        }

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic/card?page=0")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic/card?page=1")
                .then()
                .statusCode(200)
                .body(".", hasSize(pageSize));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic/card?page=2")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/content/comic/card?page=3")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void shouldCreate() {
        comicRepository.deleteAll();

        int seriesNum = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        Comic comic1 = new Comic(true, seriesNum);
        comic1.setId(1L);
        comic1.setTitle(title);
        comic1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new ComicDTO(comic1))
                .when()
                .post("/api/v1/content/comic")
                .then()
                .statusCode(200)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/comic/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(response.path("id")));
    }

    @Test
    void shouldUpdate() {
        comicRepository.deleteAll();

        int seriesNum = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";
        String changed = "Changed";

        Comic comic1 = new Comic(true, seriesNum);
        comic1.setId(1L);
        comic1.setTitle(title);
        comic1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new ComicDTO(comic1))
                .when()
                .post("/api/v1/content/comic")
                .then()
                .statusCode(200)
                .extract().response();

        Integer extractedId = response.path("id");
        comic1.setId(extractedId.longValue());
        comic1.setTitle(changed);

        given()
                .contentType(ContentType.JSON)
                .body(new ComicDTO(comic1))
                .when()
                .put("/api/v1/content/comic")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/comic/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(response.path("id")))
                .body("title", equalTo(changed));
    }

    @Test
    void shouldDelete() {
        comicRepository.deleteAll();

        int seriesNum = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        Comic comic1 = new Comic(true, seriesNum);
        comic1.setId(1L);
        comic1.setTitle(title);
        comic1.setDescription(desc);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new ComicDTO(comic1))
                .when()
                .post("/api/v1/content/comic")
                .then()
                .statusCode(200)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .when()
                .delete("/api/v1/content/comic/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/comic/{id}")
                .then()
                .statusCode(404);

        given()
                .pathParam("id", response.path("id"))
                .when()
                .get("/api/v1/content/comic/{id}/card")
                .then()
                .statusCode(404);
    }

}