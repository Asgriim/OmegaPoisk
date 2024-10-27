package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Anime;
import org.springframework.data.jdbc.repository.query.Query;

public interface AnimeRepository extends BaseContentPagingRepository<Anime> {

    @Query("SELECT COUNT(*) FROM anime")
    long countAll();
}
