package org.omega.omegapoisk.repository.tag;

import org.omega.omegapoisk.entity.tag.Tag;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    @Query("SELECT * FROM content_tags join public.tags t on t.id = content_tags.tag_id where content_id = :contendId ")
    List<Tag> findByContentId(@Param("contendId") long contentId);

    @Modifying
    @Query("DELETE FROM content_tags WHERE content_id = :contentId and tag_id = :tagId")
    void deleteByContentIdAndTagId(long contentId, long tagId);

    @Modifying
    @Query("INSERT INTO content_tags(content_id, tag_id) VALUES (:contentId, :tagId)")
    void addTagToContent(long contentId, long tagId);
}
