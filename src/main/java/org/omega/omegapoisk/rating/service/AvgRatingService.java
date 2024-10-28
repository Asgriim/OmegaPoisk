package org.omega.omegapoisk.rating.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.rating.entity.AvgRating;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvgRatingService {
    private final AvgRatingRepository avgRatingRepository;

    public AvgRating getAvgRatingByContentId(final long contentId) {
        return avgRatingRepository.findByContentId(contentId).orElse(null);
    }
}
