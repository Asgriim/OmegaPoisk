package org.omega.omegapoisk.service.rating;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.rating.AvgRating;
import org.omega.omegapoisk.entity.rating.Rating;
import org.omega.omegapoisk.repository.rating.AvgRatingRepository;
import org.omega.omegapoisk.repository.rating.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final AvgRatingRepository avgRatingRepository;

    public AvgRating getAvgRatingByContentId(final long contentId) {
        return avgRatingRepository.findByContentId(contentId).orElse(null);
    }

    public Rating findByUserAndContentId(final int contentId, final int userId) {
        return ratingRepository.findByContentIdAndUserId(contentId, userId);
    }

    @Transactional
    public Rating create(final Rating rating) {
        return ratingRepository.save(rating);
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
