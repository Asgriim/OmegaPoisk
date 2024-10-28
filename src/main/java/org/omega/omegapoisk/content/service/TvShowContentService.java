package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.TvShow;
import org.omega.omegapoisk.content.repository.TvShowRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.omega.omegapoisk.rating.service.AvgRatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class TvShowContentService extends AbstractContentService<TvShow> {
    private final TvShowRepository tvShowRepository;
    private final AvgRatingService avgRatingService;

    @Value("${spring.application.page}")
    private long page;

    public TvShowContentService(TvShowRepository tvShowRepository, AvgRatingService avgRatingService) {
        super(tvShowRepository, avgRatingService);
        this.tvShowRepository = tvShowRepository;
        this.avgRatingService = avgRatingService;
    }
}
