package org.omega.contentservice.repository;

import org.omega.contentservice.entity.Movie;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends BaseContentPagingRepository<Movie> {

    @Override
    @Query("SELECT * FROM movie LIMIT :limit OFFSET :offset")
    List<Movie> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}
