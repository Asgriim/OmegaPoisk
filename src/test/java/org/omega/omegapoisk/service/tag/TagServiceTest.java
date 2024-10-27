package org.omega.omegapoisk.service.tag;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.tag.entity.Tag;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.tag.repository.TagRepository;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class TagServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Autowired
    TagService tagService;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    AnimeContentService animeContentService;

    @Autowired
    AnimeRepository animeRepository;

    @Value("${spring.application.page-size}")
    int pageSize;

    private Anime anime;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
        animeRepository.deleteAll();

        anime = animeContentService.create(new Anime(13));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreate() {
        tagService.create(new Tag(0, "action"));
        assertThat(tagService.getAll()).hasSize(1);

        tagService.create(new Tag(0, "mystery"));
        assertThat(tagService.getAll()).hasSize(2);

        tagService.create(new Tag(0, "romance"));
        tagService.create(new Tag(0, "fantasy"));
        assertThat(tagService.getAll()).hasSize(4);
    }

    @Test
    void shouldAddTagToContent() {
        Tag tag = tagService.create(new Tag(0, "action"));
        tagService.addTagToContent(anime.getId(), tag.getId());

        assertThat(tagService.findByContentId(anime.getId())).isEqualTo(List.of(tag));
    }

    @Test
    void shouldDeleteByContent() {
        Tag tag = tagService.create(new Tag(0, "action"));
        tagService.addTagToContent(anime.getId(), tag.getId());

        tagService.deleteByContentId(anime.getId(), tag.getId());
        assertThat(tagService.findByContentId(anime.getId())).isEqualTo(List.of());

    }

    @Test
    void shouldDelete() {
        Tag tag = tagService.create(new Tag(0, "action"));
        tagService.addTagToContent(anime.getId(), tag.getId());

        tagService.delete(tag.getId());
        assertThat(tagService.getAll()).hasSize(0);

    }

}