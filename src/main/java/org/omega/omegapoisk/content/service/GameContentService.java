package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.repository.BaseContentPagingRepository;
import org.omega.omegapoisk.content.repository.GameRepository;
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
public class GameContentService extends AbstractContentService<Game>{
    private final GameRepository gameRepository;
    private final AvgRatingService avgRatingService;

    @Value("${spring.application.page}")
    private long page;

    public GameContentService(AvgRatingService avgRatingService, GameRepository gameRepository) {
        super(gameRepository, avgRatingService);
        this.gameRepository = gameRepository;
        this.avgRatingService = avgRatingService;
    }
}
