package org.omega.contentservice.service;

import lombok.Getter;
import org.omega.contentservice.entity.Comic;
import org.omega.contentservice.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class ComicContentService extends AbstractContentService<Comic> {

    @Value("${spring.application.page}")
    private long page;

    public ComicContentService(ComicRepository comicRepository, AvgRatingService avgRatingService) {
        super(comicRepository, avgRatingService);
    }

}
