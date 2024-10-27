package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.repository.AnimeRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class AnimeContentService  {
    private final AnimeRepository animeRepository;
    //todo avgRating Service
    private final AvgRatingRepository avgRatingRepository;
    private final ContentService<Anime> contentService;

    //todo rename PageSize to page
    @Value("${spring.application.page-size}")
    private int pageSize;

    public AnimeContentService(AnimeRepository animeRepository, AvgRatingRepository avgRatingRepository) {
        this.animeRepository = animeRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, animeRepository);
    }

    public Anime getById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    //todo no DTO
    public ContentCardDTO<Anime> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }


    public List<ContentCardDTO<Anime>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return contentService.getContentCardsPage(pageable);
    }

    public List<Anime> getAnimePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return animeRepository.findAll(pageable).stream().toList();
    }

    @Transactional
    public Anime create(Anime anime) {
        return contentService.createContent(anime);
    }

    @Transactional
    public Anime update(Anime anime) {
        return contentService.updateContent(anime);
    }

    @Transactional
    public void delete(long id) {
        animeRepository.deleteById(id);
    }

    public long countAll() {
        return animeRepository.countAll();
    }
}
