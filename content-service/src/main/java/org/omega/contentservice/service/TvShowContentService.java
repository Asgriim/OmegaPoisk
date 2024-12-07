package org.omega.contentservice.service;

import lombok.Getter;
import org.omega.contentservice.entity.TvShow;
import org.omega.contentservice.repository.TvShowRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class TvShowContentService extends AbstractContentService<TvShow> {
    @Value("${spring.application.page}")
    private long page;

    public TvShowContentService(TvShowRepository tvShowRepository, AvgRatingService avgRatingService) {
        super(tvShowRepository, avgRatingService);
    }
}
