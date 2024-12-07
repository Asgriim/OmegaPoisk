package org.omega.tagservice.repository;

import org.omega.tagservice.entity.Tag;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TagRepository extends ReactiveCrudRepository<Tag, Long> {

    @Query("SELECT * FROM content_tags join public.tags t on t.id = content_tags.tag_id where content_id = :contendId ")
    Flux<Tag> findByContentId(@Param("contendId") long contentId);

    @Modifying
    @Query("DELETE FROM content_tags WHERE content_id = :contentId and tag_id = :tagId")
    Mono<Void> deleteByContentIdAndTagId(long contentId, long tagId);

    @Modifying
    @Query("INSERT INTO content_tags(content_id, tag_id) VALUES (:contentId, :tagId)")
    Mono<Void> addTagToContent(long contentId, long tagId);
}
