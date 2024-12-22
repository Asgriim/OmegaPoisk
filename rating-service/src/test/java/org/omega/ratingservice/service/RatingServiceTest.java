package org.omega.ratingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.ratingservice.controller.RatingController;
import org.omega.ratingservice.entity.Rating;
import org.omega.ratingservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@MockBean(RatingController.class)
@MockBean(KafkaProducerService.class)
@Testcontainers
class RatingServiceTest {
    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    static Rating rating;

    @Value("${spring.application.page}")
    int pageSize;

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
        rating = new Rating(0, 4, 2 , 1);
        ratingRepository.deleteAll();
    }

    @Test
    void findByUserAndContentId() {
        ratingRepository.save(rating);

        assertThat(ratingService.findByContentIdAndUserId(rating.getContentId(), rating.getUserId()).getValue())
                .isEqualTo(rating.getValue());

    }

    @Test
    void create() {
        ratingService.create(rating);

        assertThat(ratingService.findByContentIdAndUserId(rating.getContentId(), rating.getUserId()).getValue())
                .isEqualTo(rating.getValue());
    }

    @Test
    void delete() {
        Rating saved = ratingService.create(rating);
        ratingService.delete(saved.getId());

        assertThat(ratingService.findByContentIdAndUserId(rating.getContentId(), rating.getUserId()))
                .isNull();
    }

    @Test
    void update() {
        Rating saved = ratingService.create(rating);
        saved.setValue(5);
        ratingService.update(saved);
        assertThat(
                ratingService.findByContentIdAndUserId(rating.getContentId(), rating.getUserId()).getValue()
        ).isEqualTo(5);
    }

    @Test
    void updateNothing() {
        ratingService.update(rating);
        assertThat(
                ratingService.findByContentIdAndUserId(rating.getContentId(), rating.getUserId()).getValue()
        ).isEqualTo(rating.getValue());
    }

}