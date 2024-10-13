package org.omega.omegapoisk.repository.rating;

import org.omega.omegapoisk.entity.rating.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, CrudRepository<Review,Long> {
}
