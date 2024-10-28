package org.omega.omegapoisk.content.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.content.repository.BaseContentPagingRepository;
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
public class AnimeContentService extends AbstractContentService<Anime> {
    private final AnimeRepository animeRepository;
    private final AvgRatingService avgRatingService;

    @Value("${spring.application.page}")
    private int page;

    public AnimeContentService(AnimeRepository animeRepository, AvgRatingService avgRatingService) {
        super(animeRepository, avgRatingService);
        this.animeRepository = animeRepository;
        this.avgRatingService = avgRatingService;
    }

}
