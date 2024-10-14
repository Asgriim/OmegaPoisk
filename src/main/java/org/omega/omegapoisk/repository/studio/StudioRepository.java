package org.omega.omegapoisk.repository.studio;

import org.omega.omegapoisk.entity.studio.Studio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudioRepository extends PagingAndSortingRepository<Studio, Long>, CrudRepository<Studio, Long> {

    @Query("INSERT INTO studio_contents(STUDIO_ID, CONTENT_ID)  VALUES(:studioId, :contentId) ")
    void addContentToStudioById(long studioId, long contentId);


    @Query("DELETE FROM studio_contents WHERE studio_id = :studioId AND content_id = :contentId")
    void removeContentFromStudioById(long studioId, long contentId);
}
