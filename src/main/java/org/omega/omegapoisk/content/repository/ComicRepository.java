package org.omega.omegapoisk.content.repository;

import org.omega.omegapoisk.content.entity.Comic;
import org.springframework.data.jdbc.repository.query.Query;

public interface ComicRepository extends BaseContentPagingRepository<Comic> {
    @Query("SELECT COUNT(*) FROM comic")
    long countAll();
}

