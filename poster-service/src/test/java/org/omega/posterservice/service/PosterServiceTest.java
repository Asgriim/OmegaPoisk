package org.omega.posterservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.posterservice.entity.Poster;
import org.omega.posterservice.repository.PosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PosterServiceTest {

    @Autowired
    private PosterService posterService;

    @Autowired
    private PosterRepository posterRepository;

    static Poster poster;

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

    @BeforeEach
    void beforeEach() {
        String originalData = "some binary data";
        String base64EncodedData = Base64.getEncoder().encodeToString(originalData.getBytes());

        byte[] data = Base64.getDecoder().decode(base64EncodedData);

        poster = new Poster(null, 1, data);
        posterRepository.deleteAll();
    }

    @Test
    void save() {
        posterService.save(poster);

        Poster savedPoster = posterRepository.findById(poster.getId()).orElse(null);
        assertNotNull(savedPoster);
        assertEquals(poster.getId(), savedPoster.getId());
        assertEquals(poster.getContentId(), savedPoster.getContentId());
        assertArrayEquals(poster.getData(), savedPoster.getData());
    }

    @Test
    void getByContentId() {
        posterService.save(poster);

        Poster foundPoster = posterService.getByContentId(poster.getContentId());
        assertNotNull(foundPoster);
        assertEquals(poster.getContentId(), foundPoster.getContentId());
        assertArrayEquals(poster.getData(), foundPoster.getData());
    }
}
