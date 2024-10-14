package org.omega.omegapoisk.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.repository.content.AnimeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeContentService  {
    private final AnimeRepository animeRepository;
    private final ContentService contentService;

    public List<Anime> getAll() {
        return (List<Anime>) animeRepository.findAll();
    }

    public Anime getById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<Anime> getContentCardById(Long id) {
        return contentService.getCardById(animeRepository, id);
    }

    public List<ContentCardDTO<Anime>> getAllCards() {
        return contentService.getAllContentCards(animeRepository);
    }

    public List<ContentCardDTO<Anime>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, contentService.getPageSize());
        return contentService.getContentCardsPage(animeRepository, pageable);
    }

    public List<Anime> getAnimePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, contentService.getPageSize());
        return animeRepository.findAll(pageable).stream().toList();
    }

}
