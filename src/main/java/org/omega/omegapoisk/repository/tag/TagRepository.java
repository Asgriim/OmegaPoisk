package org.omega.omegapoisk.repository.tag;

import org.omega.omegapoisk.entity.tag.Tag;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    @Query("SELECT * FROM content_tags join public.tags t on t.id = content_tags.tag_id where content_id = :contendId ")
    List<Tag> findByContentId(@Param("contendId") long contentId);
}
