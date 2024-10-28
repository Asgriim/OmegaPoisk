package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Comic;
import org.omega.omegapoisk.content.repository.BaseContentPagingRepository;
import org.omega.omegapoisk.content.repository.ComicRepository;
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
public class ComicContentService extends AbstractContentService<Comic> {
    private final ComicRepository comicRepository;
    private final AvgRatingService avgRatingService;

    @Value("${spring.application.page}")
    private int page;

    public ComicContentService(ComicRepository comicRepository, AvgRatingService avgRatingService) {
        super(comicRepository, avgRatingService);
        this.comicRepository = comicRepository;
        this.avgRatingService = avgRatingService;
    }

}
