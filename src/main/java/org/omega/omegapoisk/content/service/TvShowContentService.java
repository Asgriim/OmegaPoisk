package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.TvShow;
import org.omega.omegapoisk.content.repository.TvShowRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class TvShowContentService {
    private final TvShowRepository tvShowRepository;
    private final AvgRatingRepository avgRatingRepository;
    private final ContentService<TvShow> contentService;


    @Value("${spring.application.page-size}")
    private int pageSize;

    public TvShowContentService(TvShowRepository TvShowRepository, AvgRatingRepository avgRatingRepository) {
        this.tvShowRepository = TvShowRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, TvShowRepository);
    }

    public TvShow getById(Long id) {
        return tvShowRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<TvShow> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }


    public List<ContentCardDTO<TvShow>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return contentService.getContentCardsPage(pageable);
    }

    public List<TvShow> getTvShowPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return tvShowRepository.findAll(pageable).stream().toList();
    }

    @Transactional
    public TvShow create(TvShow TvShow) {
        return contentService.createContent(TvShow);
    }

    @Transactional
    public TvShow update(TvShow TvShow) {
        return contentService.updateContent(TvShow);
    }

    @Transactional
    public void delete(long id) {
        tvShowRepository.deleteById(id);
    }

    public long countAll() {
        return tvShowRepository.countAll();
    }
}
