package org.omega.tagservice.controller;

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
import org.omega.tagservice.dto.TagDTO;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.repository.TagRepository;
import org.omega.tagservice.service.TagService;
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

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagControllerTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    static Tag tag;

    @Value("${tag-service.token}")
    String token;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/test");
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
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

        tag = new Tag(0, "Test tag 2");
        tagRepository.deleteAll().block();
    }


    @Test
    void getContentTags() {
        Tag created = tagService.create(tag).block();
        tagService.addTagToContent(1, created.getId()).block();

        given()
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags/content/{contId}")
                .then()
                .statusCode(200)
                .body(".", hasItem(hasEntry("name", tag.getName())));
    }

    @Test
    void getAll() {
        tagService.create(new Tag(0L, "Test tag 1")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));

        tagService.create(new Tag(0L, "Test tag 2")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

        tagService.create(new Tag(0L, "Test tag 3")).block();
        tagService.create(new Tag(0L, "Test tag 4")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags")
                .then()
                .statusCode(200)
                .body(".", hasSize(4));
    }

    @Test
    void createTag() {

        given()
                .contentType(ContentType.JSON)
                .body(new TagDTO(tag))
                .when()
                .post("/api/v1/tags")
                .then()
                .statusCode(401);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new TagDTO(tag))
                .when()
                .post("/api/v1/tags")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags")
                .then()
                .statusCode(200)
                .body("name", contains(tag.getName()));

    }

    @Test
    void addContentTag() {
        Tag created = tagService.create(tag).block();

        given()
                .pathParam("id", created.getId())
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .post("/api/v1/tags/{id}/content/{contId}")
                .then()
                .statusCode(204);

        given()
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags/content/{contId}")
                .then()
                .statusCode(200)
                .body(".", hasItem(hasEntry("name", tag.getName())));

    }

    @Test
    void deleteTag() {

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new TagDTO(tag))
                .when()
                .post("/api/v1/tags")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/tags/{id}")
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void deleteContentTag() {
        Tag created = tagService.create(tag).block();
        tagService.addTagToContent(1, created.getId()).block();

        given()
                .pathParam("id", created.getId())
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/tags/{id}/content/{contId}")
                .then()
                .statusCode(204);

        given()
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/tags/content/{contId}")
                .then()
                .statusCode(200)
                .body(".", not(hasItem(hasEntry("name", tag.getName()))));

    }
}