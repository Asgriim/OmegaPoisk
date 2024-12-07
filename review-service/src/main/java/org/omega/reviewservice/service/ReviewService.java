package org.omega.reviewservice.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.omega.reviewservice.entity.Review;
import org.omega.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Value("${spring.application.page}")
    private int page;

    public List<Review> getPageByContentId(Pageable pageable, long contentId) {
        return reviewRepository.findByContentId(contentId, pageable).stream().toList();
    }

    @Transactional
    public Review create(final Review review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public void delete(final long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public Review update(final Review rating) {
        Review r2 = reviewRepository.findByContentIdAndUserId(rating.getContentId(), rating.getUserId());
        if (r2 != null) {
            rating.setId(r2.getId());
        }
        return reviewRepository.save(rating);
    }


    public long countAllByContentId(final Long contentId) {
        return reviewRepository.countAllByContentId(contentId);
    }


}
