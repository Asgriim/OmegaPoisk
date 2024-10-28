package org.omega.omegapoisk.rating.repository;

import org.omega.omegapoisk.rating.entity.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    Rating findByContentIdAndUserId(long contentId, long userId);
}
