package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Movie;
import org.omega.omegapoisk.content.repository.MovieRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class MovieContentService {
    private final MovieRepository movieRepository;
    private final AvgRatingRepository avgRatingRepository;
    private final ContentService<Movie> contentService;

    @Value("${spring.application.page-size}")
    private int pageSize;

    public MovieContentService(MovieRepository MovieRepository, AvgRatingRepository avgRatingRepository) {
        this.movieRepository = MovieRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, MovieRepository);
    }

    public Movie getById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<Movie> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }


    public List<ContentCardDTO<Movie>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return contentService.getContentCardsPage(pageable);
    }

    public List<Movie> getMoviePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return movieRepository.findAll(pageable).stream().toList();
    }

    @Transactional
    public Movie create(Movie Movie) {
        return contentService.createContent(Movie);
    }

    @Transactional
    public Movie update(Movie Movie) {
        return contentService.updateContent(Movie);
    }

    @Transactional
    public void delete(long id) {
        movieRepository.deleteById(id);
    }

    public long countAll() {
        return movieRepository.countAll();
    }
}
