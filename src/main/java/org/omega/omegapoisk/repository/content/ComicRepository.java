package org.omega.omegapoisk.repository.content;

import org.omega.omegapoisk.entity.content.Comic;
import org.springframework.data.jdbc.repository.query.Query;

public interface ComicRepository extends BaseContentPagingRepository<Comic> {
    @Query("SELECT COUNT(*) FROM comic")
    long countAll();
}

