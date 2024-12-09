package org.omega.tagservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    static Tag tag;

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

    @BeforeEach
    void beforeEach() {
        tag = new Tag(0, "Test tag");
        tagRepository.deleteAll().block();
    }


    @Test
    void findByContentId() {
        Tag saved = tagService.create(tag).block();
        tagRepository.addTagToContent(1, saved.getId()).block();

        tagService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextMatches(tag -> tag.getName().equals(saved.getName()))
                .verifyComplete();
    }

    @Test
    void getAll() {
        tagRepository.save(new Tag(0, "Test tag 1")).block();
        tagService.getAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        tagRepository.save(new Tag(0, "Test tag 2")).block();
        tagService.getAll()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();

        tagRepository.save(new Tag(0, "Test tag 3")).block();
        tagRepository.save(new Tag(0, "Test tag 4")).block();
        tagService.getAll()
                .as(StepVerifier::create)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void create() {
        tagService.create(tag).block();

        tagService.getAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void addTagToContent() {
        Tag saved = tagService.create(tag).block();
        tagService.addTagToContent(1, saved.getId()).block();

        tagService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextMatches(tag -> tag.getName().equals(saved.getName()))
                .verifyComplete();
    }

    @Test
    void deleteByContentId() {
        Tag saved = tagService.create(tag).block();
        tagService.addTagToContent(1, saved.getId()).block();
        tagService.deleteByContentId(1, saved.getId()).block();

        tagService.findByContentId(1)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void delete() {
        Tag saved = tagRepository.save(tag).block();

        tagService.getAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        tagService.delete(saved.getId())
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
}