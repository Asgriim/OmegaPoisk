package org.omega.contentservice.service;

import lombok.Getter;
import org.omega.contentservice.entity.Anime;
import org.omega.contentservice.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class AnimeContentService extends AbstractContentService<Anime> {

    @Value("${spring.application.page}")
    private long page;

    public AnimeContentService(AnimeRepository animeRepository, AvgRatingService avgRatingService) {
        super(animeRepository, avgRatingService);
    }

}
