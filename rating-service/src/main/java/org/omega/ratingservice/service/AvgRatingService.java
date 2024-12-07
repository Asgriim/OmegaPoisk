package org.omega.ratingservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.ratingservice.entity.AvgRating;
import org.omega.ratingservice.repository.AvgRatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvgRatingService {
    private final AvgRatingRepository avgRatingRepository;

    public AvgRating getAvgRatingByContentId(final long contentId) {
        return avgRatingRepository.findByContentId(contentId).orElse(null);
    }
}
