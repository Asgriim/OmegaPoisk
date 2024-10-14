package org.omega.omegapoisk.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Content;
import org.omega.omegapoisk.entity.rating.AvgRating;
import org.omega.omegapoisk.repository.content.BaseContentPagingRepository;
import org.omega.omegapoisk.repository.rating.AvgRatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Getter
public class ContentService {
    @Value("${spring.application.page-size}")
    private int pageSize;

    private final AvgRatingRepository avgRatingRepository;

    <T extends Content> ContentCardDTO<T> createCard(T content) {
        Optional<AvgRating> avgRating = avgRatingRepository.findByContentId(content.getId());
        ContentCardDTO<T> dto = new ContentCardDTO<>();
        dto.setContent(content);
        avgRating.ifPresent(rating -> dto.setAvgRating(rating.getAvgRate()));
        return dto;
    }

    <T extends Content> List<ContentCardDTO<T>> createContentCardDTOList(Iterable<T> iterable) {
        List<ContentCardDTO<T>> result = new ArrayList<>();

        iterable.forEach(
                c -> result.add(createCard(c))
        );

        return result;
    }

    <T extends Content> List<ContentCardDTO<T>> getAllContentCards(BaseContentPagingRepository<T> repository) {
        Iterable<T> all = repository.findAll();
        return createContentCardDTOList(all);
    }

    <T extends Content> List<ContentCardDTO<T>> getContentCardsPage(BaseContentPagingRepository<T> repository, Pageable pageable) {
        Page<T> all = repository.findAll(pageable);
        return createContentCardDTOList(all.getContent());
    }

    <T extends Content> ContentCardDTO<T> getCardById(BaseContentPagingRepository<T> repository, Long id) {
        T content = repository.findById(id).orElse(null);
        if (content == null) {
            return new ContentCardDTO<T>();
        }
        return createCard(content);
    }

}
