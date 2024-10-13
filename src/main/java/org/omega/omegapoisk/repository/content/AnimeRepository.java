package org.omega.omegapoisk.repository.content;

import org.omega.omegapoisk.entity.content.Anime;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

public interface AnimeRepository extends BaseContentPagingRepository<Anime> {

    @Query("SELECT COUNT(*) FROM anime")
    long countAll();
}
