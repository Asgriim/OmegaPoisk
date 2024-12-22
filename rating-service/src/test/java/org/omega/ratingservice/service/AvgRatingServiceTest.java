package org.omega.ratingservice.service;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.ratingservice.controller.RatingController;
import org.omega.ratingservice.entity.AvgRating;
import org.omega.ratingservice.entity.Rating;
import org.omega.ratingservice.repository.AvgRatingRepository;
import org.omega.ratingservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class AvgRatingServiceTest {

    @Autowired
    private AvgRatingService avgRatingService;

    @Autowired
    private RatingRepository ratingRepository;

    static AvgRating avgRating;

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
        ratingRepository.deleteAll();
    }

    @Test
    void getAvgRatingByContentId() {
        int contentId = 1;
        ratingRepository.save(new Rating(0, 4, 2 , contentId));

        assertThat(avgRatingService.getAvgRatingByContentId(contentId).getAvgRate())
                .isEqualTo(4, Offset.offset(1e-6));

        ratingRepository.save(new Rating(0, 5, 1 , contentId));

        assertThat(avgRatingService.getAvgRatingByContentId(contentId).getAvgRate())
                .isEqualTo(4.5, Offset.offset(1e-6));


    }
}