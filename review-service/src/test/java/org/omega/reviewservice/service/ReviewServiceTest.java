package org.omega.reviewservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.reviewservice.controller.ReviewController;
import org.omega.reviewservice.entity.Review;
import org.omega.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@MockBean(ReviewController.class)
@MockBean(KafkaProducerService.class)
@Testcontainers
class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    static Review review;

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
        review = new Review(0, "Imba", 2 , 1);
        reviewRepository.deleteAll();
    }

    @Test
    void getPageByContentId() {
        for (int i = 0; i < pageSize*2+2; i++) {
            reviewRepository.save(new Review(0, "Imba" + i, 2 , 1));
        }

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                )
        ).hasSize(pageSize);

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(1, pageSize), review.getContentId()
                )
        ).hasSize(pageSize);

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(2, pageSize), review.getContentId()
                )
        ).hasSize(2);

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(3, pageSize), review.getContentId()
                )
        ).hasSize(0);
    }

    @Test
    void create() {
        reviewService.create(review);

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                ).get(0).getTxt()
        ).isEqualTo(review.getTxt());
    }

    @Test
    void delete() {
        Review saved = reviewService.create(review);
        reviewService.delete(saved.getId());

        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                )
        ).hasSize(0);
    }

    @Test
    void update() {
        Review saved = reviewService.create(review);
        saved.setTxt("Changed");
        reviewService.update(saved);
        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                ).get(0).getTxt()
        ).isEqualTo("Changed");

    }

    @Test
    void updateNothing() {
        reviewService.update(review);
        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                ).get(0).getTxt()
        ).isEqualTo(review.getTxt());

    }

    @Test
    void countAllByContentId() {
        reviewRepository.save(new Review(0, "Imba 1", 2 , 1));
        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                )
        ).hasSize(1);

        reviewRepository.save(new Review(0, "Imba 2", 2 , 1));
        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                )
        ).hasSize(2);

        reviewRepository.save(new Review(0, "Imba 3", 2 , 1));
        reviewRepository.save(new Review(0, "Imba 4", 2 , 1));
        assertThat(
                reviewService.getPageByContentId(
                        PageRequest.of(0, pageSize), review.getContentId()
                )
        ).hasSize(4);
    }
}