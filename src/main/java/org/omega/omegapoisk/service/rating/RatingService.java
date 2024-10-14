package org.omega.omegapoisk.service.rating;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.rating.AvgRating;
import org.omega.omegapoisk.entity.rating.Rating;
import org.omega.omegapoisk.repository.rating.AvgRatingRepository;
import org.omega.omegapoisk.repository.rating.RatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final AvgRatingRepository avgRatingRepository;

    AvgRating getAvgRatingByContentId(final Long contentId) {
        return avgRatingRepository.findByContentId(contentId).orElse(null);
    }

    Rating findByUserAndContentId(final Long contentId, final Long userId) {
        return ratingRepository.findByContentIdAndUserId(contentId, userId);
    }

    Rating create(final Rating rating) {
        return ratingRepository.save(rating);
    }

    void delete(Rating rating) {
        ratingRepository.delete(rating);
    }

}
