package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.TvShow;
import org.springframework.data.jdbc.repository.query.Query;

public interface TvShowRepository extends BaseContentPagingRepository<TvShow>{

    @Query("SELECT COUNT(*) FROM tv_show")
    long countAll();
}
