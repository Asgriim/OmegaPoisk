package org.omega.contentservice.repository;

import org.omega.contentservice.entity.Game;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends BaseContentPagingRepository<Game> {

    @Override
    @Query("SELECT * FROM game LIMIT :limit OFFSET :offset")
    List<Game> findRangeWithLimitOffset(@Param("limit") long limit, @Param("offset") long offset);
}
