package org.omega.omegapoisk.content.dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.omega.omegapoisk.content.entity.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class AllDTOTest {
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @Test
    void shouldConvertAnimeDTO(){
        int seriesNum = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        AnimeDTO anime1 = new AnimeDTO(seriesNum);
        anime1.setId(1L);
        anime1.setTitle(title);
        anime1.setDescription(desc);

        Anime anime = anime1.toEntity();

        assertThat(anime.getSeriesNum()).isEqualTo(anime1.getSeriesNum());
        assertThat(anime.getId()).isEqualTo(anime1.getId());
        assertThat(anime.getTitle()).isEqualTo(anime1.getTitle());
        assertThat(anime.getDescription()).isEqualTo(anime1.getDescription());
    }

    @Test
    void shouldConvertComicDTO(){
        int chapters = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        ComicDTO comic1 = new ComicDTO(true, chapters);
        comic1.setId(1L);
        comic1.setTitle(title);
        comic1.setDescription(desc);

        Comic comic = comic1.toEntity();

        assertThat(comic.isColored()).isEqualTo(comic1.isColored());
        assertThat(comic.getChaptersCount()).isEqualTo(comic1.getChaptersCount());
        assertThat(comic.getId()).isEqualTo(comic1.getId());
        assertThat(comic.getTitle()).isEqualTo(comic1.getTitle());
        assertThat(comic.getDescription()).isEqualTo(comic1.getDescription());
    }

    @Test
    void shouldConvertGameDTO(){
        String title = "86 2";
        String desc = "Robots.txt 2";

        GameDTO game1 = new GameDTO(false);
        game1.setId(1L);
        game1.setTitle(title);
        game1.setDescription(desc);

        Game game = game1.toEntity();

        assertThat(game.isFree()).isEqualTo(game1.isFree());
        assertThat(game.getId()).isEqualTo(game1.getId());
        assertThat(game.getTitle()).isEqualTo(game1.getTitle());
        assertThat(game.getDescription()).isEqualTo(game1.getDescription());
    }

    @Test
    void shouldConvertMovieDTO(){
        int duration = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        MovieDTO anime1 = new MovieDTO(duration);
        anime1.setId(1L);
        anime1.setTitle(title);
        anime1.setDescription(desc);

        Movie movie = anime1.toEntity();

        assertThat(movie.getDuration()).isEqualTo(anime1.getDuration());
        assertThat(movie.getId()).isEqualTo(anime1.getId());
        assertThat(movie.getTitle()).isEqualTo(anime1.getTitle());
        assertThat(movie.getDescription()).isEqualTo(anime1.getDescription());
    }

    @Test
    void shouldConvertTvShowDTO(){
        int seriesNum = 14;
        String title = "86 2";
        String desc = "Robots.txt 2";

        TvShowDTO tvShow1 = new TvShowDTO(seriesNum);
        tvShow1.setId(1L);
        tvShow1.setTitle(title);
        tvShow1.setDescription(desc);

        TvShow tvShow = tvShow1.toEntity();

        assertThat(tvShow.getSeriesNum()).isEqualTo(tvShow1.getSeriesNum());
        assertThat(tvShow.getId()).isEqualTo(tvShow1.getId());
        assertThat(tvShow.getTitle()).isEqualTo(tvShow1.getTitle());
        assertThat(tvShow.getDescription()).isEqualTo(tvShow1.getDescription());
    }

}