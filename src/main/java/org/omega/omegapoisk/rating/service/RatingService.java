package org.omega.omegapoisk.rating.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.rating.entity.AvgRating;
import org.omega.omegapoisk.rating.entity.Rating;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.omega.omegapoisk.rating.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Rating findByUserAndContentId(final int contentId, final int userId) {
        return ratingRepository.findByContentIdAndUserId(contentId, userId);
    }

    @Transactional
    public Rating create(final Rating rating) {
        return update(rating);
    }

    @Transactional
    public Rating update(final Rating rating) {
        Rating r2 = findByUserAndContentId(rating.getContentId(), rating.getUserId());
        if (r2 != null) {
            rating.setId(r2.getId());
        }
        return ratingRepository.save(rating);
    }

    @Transactional
    public void delete(long id) {
        ratingRepository.deleteById(id);
    }
}
