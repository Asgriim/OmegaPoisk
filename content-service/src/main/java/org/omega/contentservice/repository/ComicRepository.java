package org.omega.contentservice.repository;

import org.omega.contentservice.entity.Comic;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComicRepository extends BaseContentPagingRepository<Comic> {

    @Override
    @Query("SELECT * FROM comic LIMIT :limit OFFSET :offset")
    List<Comic> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}

