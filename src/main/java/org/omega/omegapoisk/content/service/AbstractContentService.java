package org.omega.omegapoisk.content.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.entity.Content;
import org.omega.omegapoisk.content.entity.ContentCard;
import org.omega.omegapoisk.content.repository.BaseContentPagingRepository;
import org.omega.omegapoisk.rating.entity.AvgRating;
import org.omega.omegapoisk.rating.service.AvgRatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractContentService <T extends Content> {
    private final BaseContentPagingRepository<T> contentRepository;
    private final AvgRatingService avgRatingService;

    private ContentCard<T> createCard(T content) {
        ContentCard<T> card = new ContentCard<>();
        if (content != null) {
            card.setContent(content);
            AvgRating avgRating = avgRatingService.getAvgRatingByContentId(content.getId());

            if (avgRating != null) {
                card.setAvgRating(avgRating);
            }
        }
        return card;
    }

    private List<ContentCard<T>> createCardList(List<T> contentList) {
        return contentList.stream().map(this::createCard).toList();
    }

    public List<T> getContentPage(Pageable pageable) {
        return contentRepository.findAll(pageable).getContent();
    }

    public List<T> getContentPageRange(Pageable pageableFrom, Pageable pageableTo) {
        long offset = pageableFrom.getOffset();
        long limit = (long) (pageableTo.getPageNumber() - pageableFrom.getPageNumber() + 1) * pageableTo.getPageSize();

        return contentRepository.findRangeWithLimitOffset(limit, offset);
    }

    public T getContentById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    @Transactional
    public T createContent(T content) {
        content.setId(contentRepository.getNextContentId());
        content.setNew(true);
        return contentRepository.save(content);
    }

    @Transactional
    public T updateContent(T content) {
        content.setNew(false);
        return contentRepository.save(content);
    }

    @Transactional
    public void delete(T content) {
        contentRepository.deleteById(content.getId());
    }

    public long count() {
        return contentRepository.count();
    }

    public List<ContentCard<T>> getContentCardPage(Pageable pageable) {
        return createCardList(getContentPage(pageable));
    }

    private List<ContentCard<T>> getContentCardPageRange(Pageable pageableFrom, Pageable pageableTo) {
        return createCardList(getContentPageRange(pageableFrom, pageableTo));
    }

    public ContentCard<T> getContentCardById(Long id) {
        return createCard(contentRepository.findById(id).orElse(null));
    }


}

