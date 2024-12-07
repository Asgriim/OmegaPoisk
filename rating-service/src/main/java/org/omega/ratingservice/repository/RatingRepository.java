package org.omega.ratingservice.repository;


import org.omega.ratingservice.entity.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    Rating findByContentIdAndUserId(long contentId, long userId);
}
