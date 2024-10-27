package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Movie;
import org.springframework.data.jdbc.repository.query.Query;

public interface MovieRepository extends BaseContentPagingRepository<Movie> {
    @Query("SELECT COUNT(*) FROM movie")
    long countAll();
}
