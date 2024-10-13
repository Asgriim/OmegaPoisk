package org.omega.omegapoisk.repository.content;

import org.omega.omegapoisk.entity.content.Movie;
import org.springframework.data.jdbc.repository.query.Query;

public interface MovieRepository extends BaseContentPagingRepository<Movie> {
    @Query("SELECT COUNT(*) FROM movie")
    long countAll();
}
