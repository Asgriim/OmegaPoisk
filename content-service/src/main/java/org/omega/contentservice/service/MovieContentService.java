package org.omega.contentservice.service;

import lombok.Getter;
import org.omega.contentservice.entity.Movie;
import org.omega.contentservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class MovieContentService extends AbstractContentService<Movie> {
    @Value("${spring.application.page}")
    private long page;

    public MovieContentService(MovieRepository movieRepository, AvgRatingService avgRatingService) {
        super(movieRepository, avgRatingService);
    }
}
