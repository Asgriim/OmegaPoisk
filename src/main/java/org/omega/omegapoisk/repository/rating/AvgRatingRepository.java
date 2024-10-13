package org.omega.omegapoisk.repository.rating;

import org.omega.omegapoisk.entity.rating.AvgRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AvgRatingRepository extends CrudRepository<AvgRating, Long> {
    Optional<AvgRating> findByContentId(Long contentId);
}
