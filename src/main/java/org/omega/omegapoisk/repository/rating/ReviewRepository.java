package org.omega.omegapoisk.repository.rating;

import org.omega.omegapoisk.entity.rating.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, CrudRepository<Review,Long> {
    Page<Review> findByContentId(long contentId, Pageable pageable);
    Review findByContentIdAndUserId(long contentId, long userId);
    long countAllByContentId(long contentId);
}
