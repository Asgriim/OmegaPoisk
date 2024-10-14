package org.omega.omegapoisk.repository.studio;

import org.omega.omegapoisk.entity.studio.Studio;
import org.omega.omegapoisk.entity.tag.Tag;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudioRepository extends PagingAndSortingRepository<Studio, Long>, CrudRepository<Studio, Long> {
    @Modifying
    @Query("INSERT INTO studio_contents(STUDIO_ID, CONTENT_ID)  VALUES(:studioId, :contentId) ")
    void addContentToStudioById(long studioId, long contentId);

    @Modifying
    @Query("DELETE FROM studio_contents WHERE studio_id = :studioId AND content_id = :contentId")
    void removeContentFromStudioById(long studioId, long contentId);

    @Query("SELECT * FROM studio_contents join public.studio t on t.id = studio_contents.studio_id where content_id = :contendId ")
    List<Studio> findByContentId(@Param("contendId") long contentId);
}
