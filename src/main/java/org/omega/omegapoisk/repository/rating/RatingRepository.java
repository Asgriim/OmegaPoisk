package org.omega.omegapoisk.repository.rating;

import org.omega.omegapoisk.entity.rating.Rating;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    Rating findByContentIdAndUserId(long contentId, long userId);
}
