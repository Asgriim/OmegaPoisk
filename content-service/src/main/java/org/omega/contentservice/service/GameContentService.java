package org.omega.contentservice.service;

import lombok.Getter;
import org.omega.contentservice.entity.Game;
import org.omega.contentservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class GameContentService extends AbstractContentService<Game>{
    @Value("${spring.application.page}")
    private long page;

    public GameContentService(AvgRatingService avgRatingService, GameRepository gameRepository) {
        super(gameRepository, avgRatingService);
    }
}
