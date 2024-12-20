package org.omega.contentservice.repository;

import org.omega.contentservice.entity.Anime;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimeRepository extends BaseContentPagingRepository<Anime> {

    @Override
    @Query("SELECT * FROM anime  LIMIT :limit OFFSET :offset")
    List<Anime> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}
