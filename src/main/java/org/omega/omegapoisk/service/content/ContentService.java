package org.omega.omegapoisk.service.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Content;
import org.omega.omegapoisk.entity.rating.AvgRating;
import org.omega.omegapoisk.repository.content.BaseContentPagingRepository;
import org.omega.omegapoisk.repository.rating.AvgRatingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class ContentService <T extends Content>{
    private int pageSize;

    private final AvgRatingRepository avgRatingRepository;
    private final BaseContentPagingRepository<T> repository;

    public ContentCardDTO<T> createCard(T content) {
        Optional<AvgRating> avgRating = avgRatingRepository.findByContentId(content.getId());
        ContentCardDTO<T> dto = new ContentCardDTO<>();
        dto.setContent(content);
        avgRating.ifPresent(rating -> dto.setAvgRating(rating.getAvgRate()));
        return dto;
    }

    public List<ContentCardDTO<T>> createContentCardDTOList(Iterable<T> iterable) {
        List<ContentCardDTO<T>> result = new ArrayList<>();

        iterable.forEach(
                c -> result.add(createCard(c))
        );

        return result;
    }

    public List<ContentCardDTO<T>> getAllContentCards(BaseContentPagingRepository<T> repository) {
        Iterable<T> all = repository.findAll();
        return createContentCardDTOList(all);
    }

    public List<ContentCardDTO<T>> getContentCardsPage(Pageable pageable) {
        Page<T> all = repository.findAll(pageable);
        return createContentCardDTOList(all.getContent());
    }

    public ContentCardDTO<T> getCardById(Long id) {
        T content = repository.findById(id).orElse(null);
        if (content == null) {
            return new ContentCardDTO<T>();
        }
        return createCard(content);
    }

    @Transactional
    public T createContent(T content) {
        content.setId(repository.getNextContentId());
        content.setNew(true);
        return repository.save(content);
    }

    @Transactional
    public T updateContent(T content) {
        content.setNew(false);
        return repository.save(content);
    }

    @Transactional
    public void delete(T content) {
        repository.deleteById(content.getId());
    }

}
