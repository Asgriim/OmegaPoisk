package org.omega.omegapoisk.repository.content;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface BaseContentPagingRepository<T> extends PagingAndSortingRepository<T, Long>, CrudRepository<T, Long> {

    @Query("SELECT nextval('content_id_seq')")
    long getNextContentId();

}
