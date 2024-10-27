package org.omega.omegapoisk.content.service;

import lombok.Getter;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.repository.GameRepository;
import org.omega.omegapoisk.rating.repository.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Getter
public class GameContentService {
    private final GameRepository gameRepository;
    private final AvgRatingRepository avgRatingRepository;
    private final ContentService<Game> contentService;

    @Value("${spring.application.page-size}")
    private int pageSize;

    public GameContentService(GameRepository GameRepository, AvgRatingRepository avgRatingRepository) {
        this.gameRepository = GameRepository;
        this.avgRatingRepository = avgRatingRepository;
        this.contentService = new ContentService<>(avgRatingRepository, GameRepository);
    }

    public Game getById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public ContentCardDTO<Game> getContentCardById(Long id) {
        return contentService.getCardById(id);
    }


    public List<ContentCardDTO<Game>> getCardsPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return contentService.getContentCardsPage(pageable);
    }

    public List<Game> getGamePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return gameRepository.findAll(pageable).stream().toList();
    }

    @Transactional
    public Game create(Game Game) {
        return contentService.createContent(Game);
    }

    @Transactional
    public Game update(Game Game) {
        return contentService.updateContent(Game);
    }

    @Transactional
    public void delete(long id) {
        gameRepository.deleteById(id);
    }

    public long countAll() {
        return gameRepository.countAll();
    }
}
