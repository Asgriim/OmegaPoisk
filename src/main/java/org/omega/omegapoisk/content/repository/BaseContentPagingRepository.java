package org.omega.omegapoisk.content.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface BaseContentPagingRepository<T> extends PagingAndSortingRepository<T, Long>, CrudRepository<T, Long> {

    @Query("SELECT nextval('content_id_seq')")
    long getNextContentId();

    List<T> findRangeWithLimitOffset(long limit, long offset);
}
