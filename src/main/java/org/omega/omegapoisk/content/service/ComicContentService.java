package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Comic;
import org.omega.omegapoisk.content.repository.ComicRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class ComicContentService {
    private final ComicRepository comicRepository;
    private final AvgRatingRepository avgRatingRepository;
    private final ContentService<Comic> contentService;

    @Value("${spring.application.page-size}")
    private int pageSize;

    public ComicContentService(ComicRepository comicRepository, AvgRatingRepository avgRatingRepository) {
        this.comicRepository = comicRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, comicRepository);
    }


    public Comic getById(Long id) {
        return comicRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<Comic> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }


    public List<ContentCardDTO<Comic>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return contentService.getContentCardsPage(pageable);
    }

    public List<Comic> getComicPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return comicRepository.findAll(pageable).stream().toList();
    }

    @Transactional
    public Comic create(Comic Comic) {
        return contentService.createContent(Comic);
    }

    @Transactional
    public Comic update(Comic Comic) {
        return contentService.updateContent(Comic);
    }

    @Transactional
    public void delete(long id) {
        comicRepository.deleteById(id);
    }

    public long countAll() {
        return comicRepository.countAll();
    }

}
