package org.omega.posterservice.repository;

import org.omega.posterservice.entity.Poster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PosterRepository extends CrudRepository<Poster, Long> {
    Optional<Poster> findByContentId(Integer contentId);
}
