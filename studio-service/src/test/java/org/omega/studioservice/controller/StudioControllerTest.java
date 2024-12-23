package org.omega.studioservice.controller;

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
import org.omega.studioservice.dto.StudioDTO;
import org.omega.studioservice.entity.Studio;
import org.omega.studioservice.repository.StudioRepository;
import org.omega.studioservice.service.StudioService;
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
class StudioControllerTest {

    @Autowired
    private StudioService studioService;

    @Autowired
    private StudioRepository studioRepository;

    static Studio studio;

    @Value("${studio-service.token}")
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

        studio = new Studio(0, "Test studio 2");
        studioRepository.deleteAll();
    }


//    @Test
//    void getContentStudios() {
//        Studio created = studioService.createStudio(studio).block();
//        studioService.addContentToStudio(created.getId(), 1).block();
//
//        given()
//                .pathParam("contId", 1L)
//                .header(new Header("Authorization", "Bearer " + token.trim()))
//                .when()
//                .get("/api/v1/studio/content/{contId}")
//                .then()
//                .statusCode(200)
//                .body(".", hasItem(hasEntry("name", studio.getName())));
//    }

    @Test
    void getAll() {
        studioService.createStudio(new Studio(0L, "Test studio 1")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));

        studioService.createStudio(new Studio(0L, "Test studio 2")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

        studioService.createStudio(new Studio(0L, "Test studio 3")).block();
        studioService.createStudio(new Studio(0L, "Test studio 4")).block();
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(4));
    }

    @Test
    void createStudio() {

        given()
                .contentType(ContentType.JSON)
                .body(new StudioDTO(studio))
                .when()
                .post("/api/v1/studio")
                .then()
                .statusCode(401);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new StudioDTO(studio))
                .when()
                .post("/api/v1/studio")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body("name", contains(studio.getName()));

    }

    @Test
    void addContentStudio() {
        Studio created = studioService.createStudio(studio).block();

        given()
                .pathParam("id", created.getId())
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .post("/api/v1/studio/{id}/content/{contId}")
                .then()
                .statusCode(204);

    }

    @Test
    void deleteStudio() {

        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .body(new StudioDTO(studio))
                .when()
                .post("/api/v1/studio")
                .then()
                .statusCode(201)
                .extract().response();

        given()
                .pathParam("id", response.path("id"))
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/studio/{id}")
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .get("/api/v1/studio")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void deleteContentStudio() {
        Studio created = studioService.createStudio(studio).block();
        studioService.addContentToStudio(created.getId(), 1).block();

        given()
                .pathParam("id", created.getId())
                .pathParam("contId", 1L)
                .header(new Header("Authorization", "Bearer " + token.trim()))
                .when()
                .delete("/api/v1/studio/{id}/content/{contId}")
                .then()
                .statusCode(204);

    }
}
