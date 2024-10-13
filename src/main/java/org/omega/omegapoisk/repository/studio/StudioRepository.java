package org.omega.omegapoisk.repository.studio;

import org.omega.omegapoisk.entity.studio.Studio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudioRepository extends PagingAndSortingRepository<Studio, Long>, CrudRepository<Studio, Long> {
}
