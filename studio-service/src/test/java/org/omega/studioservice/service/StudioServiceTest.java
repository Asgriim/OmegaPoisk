package org.omega.studioservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.studioservice.entity.Studio;
import org.omega.studioservice.repository.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class StudioServiceTest {

    @Autowired
    private StudioService studioService;

    @Autowired
    private StudioRepository studioRepository;

    static Studio studio;

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
        studio = new Studio(0, "Test studio");
        studioRepository.deleteAll();
    }


    @Test
    void findByContentId() {
        Studio saved = studioService.createStudio(studio).block();
        studioRepository.addContentToStudioById(saved.getId(), 1);

        studioService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextMatches(studio -> studio.getName().equals(saved.getName()))
                .verifyComplete();
    }

    @Test
    void getAll() {
        studioRepository.save(new Studio(0, "Test studio 1"));

        studioService.getAllStudios()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        studioRepository.save(new Studio(0, "Test studio 2"));
        studioService.getAllStudios()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();

        studioRepository.save(new Studio(0, "Test studio 3"));
        studioRepository.save(new Studio(0, "Test studio 4"));
        studioService.getAllStudios()
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void create() {
        studioService.createStudio(studio).block();

        studioService.getAllStudios()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void addStudioToContent() {
        Studio saved = studioService.createStudio(studio).block();
        studioService.addContentToStudio(saved.getId(), 1).block();

        studioService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextMatches(studio -> studio.getName().equals(saved.getName()))
                .verifyComplete();
    }

    @Test
    void deleteByContentId() {
        Studio saved = studioService.createStudio(studio).block();
        studioService.addContentToStudio(saved.getId(), 1).block();
        studioService.deleteContentFromStudio(saved.getId(), 1).block();

        studioService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void delete() {
        Studio saved = studioRepository.save(studio);

        studioService.getAllStudios()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        studioService.deleteStudio(saved.getId())
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
}
