package org.omega.omegapoisk.service;

import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.repository.content.AnimeRepository;
import org.omega.omegapoisk.repository.rating.AvgRatingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnimeContentService  {
    private final AnimeRepository animeRepository;
    private final AvgRatingRepository avgRatingRepository;

    private final ContentService<Anime> contentService;

    public AnimeContentService(AnimeRepository animeRepository, AvgRatingRepository avgRatingRepository) {
        this.animeRepository = animeRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, animeRepository);
    }

    public List<Anime> getAll() {
        return (List<Anime>) animeRepository.findAll();
    }

    public Anime getById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<Anime> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }

    public List<ContentCardDTO<Anime>> getAllCards() {
        return contentService.getAllContentCards(animeRepository);
    }

    public List<ContentCardDTO<Anime>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, contentService.getPageSize());
        return contentService.getContentCardsPage(pageable);
    }

    public List<Anime> getAnimePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, contentService.getPageSize());
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

}
