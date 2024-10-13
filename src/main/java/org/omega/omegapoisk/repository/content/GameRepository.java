package org.omega.omegapoisk.repository.content;

import org.omega.omegapoisk.entity.content.Game;
import org.springframework.data.jdbc.repository.query.Query;

public interface GameRepository extends BaseContentPagingRepository<Game> {
    @Query("SELECT COUNT(*) FROM game")
    long countAll();
}
