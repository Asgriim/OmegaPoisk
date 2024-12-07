package org.omega.ratingservice.repository;


import org.omega.ratingservice.entity.AvgRating;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AvgRatingRepository extends CrudRepository<AvgRating, Long> {
    Optional<AvgRating> findByContentId(Long contentId);
}
