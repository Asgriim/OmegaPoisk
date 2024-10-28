package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Movie;
import org.omega.omegapoisk.content.repository.MovieRepository;
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
public class MovieContentService extends AbstractContentService<Movie> {
    private final MovieRepository movieRepository;
    private final AvgRatingService avgRatingService;

    @Value("${spring.application.page}")
    private int page;

    public MovieContentService(MovieRepository movieRepository, AvgRatingService avgRatingService) {
        super(movieRepository, avgRatingService);
        this.movieRepository = movieRepository;
        this.avgRatingService = avgRatingService;
    }
}
