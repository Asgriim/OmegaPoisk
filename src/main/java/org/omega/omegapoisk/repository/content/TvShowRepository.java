package org.omega.omegapoisk.repository.content;

import org.omega.omegapoisk.entity.content.TvShow;
import org.springframework.data.jdbc.repository.query.Query;

public interface TvShowRepository extends BaseContentPagingRepository<TvShow>{

    @Query("SELECT COUNT(*) FROM tv_show")
    long countAll();
}
