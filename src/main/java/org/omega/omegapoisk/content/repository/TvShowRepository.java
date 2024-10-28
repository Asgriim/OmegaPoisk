package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.entity.TvShow;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TvShowRepository extends BaseContentPagingRepository<TvShow> {

    @Override
    @Query("SELECT * FROM tv_show LIMIT :limit OFFSET :offset")
    List<TvShow> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}
