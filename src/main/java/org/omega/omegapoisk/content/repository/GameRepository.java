package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Game;
import org.springframework.data.jdbc.repository.query.Query;

public interface GameRepository extends BaseContentPagingRepository<Game> {
    @Query("SELECT COUNT(*) FROM game")
    long countAll();
}
