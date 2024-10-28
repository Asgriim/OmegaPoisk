package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.entity.Movie;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends BaseContentPagingRepository<Movie> {

    @Override
    @Query("SELECT * FROM movie LIMIT :limit OFFSET :offset")
    List<Movie> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}
