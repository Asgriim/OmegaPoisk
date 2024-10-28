package org.omega.omegapoisk.rating.repository;

import org.omega.omegapoisk.rating.entity.AvgRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AvgRatingRepository extends CrudRepository<AvgRating, Long> {
    Optional<AvgRating> findByContentId(Long contentId);
}
