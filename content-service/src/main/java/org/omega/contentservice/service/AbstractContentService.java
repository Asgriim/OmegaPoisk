package org.omega.contentservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.contentservice.entity.Content;
import org.omega.contentservice.entity.ContentCard;
import org.omega.contentservice.repository.BaseContentPagingRepository;
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
            Double avgRating = avgRatingService.getAvgRatingByContentId(content.getId());

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

    public T getById(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    @Transactional
    public T create(T content) {
        content.setId(contentRepository.getNextContentId());
        content.setNew(true);
        return contentRepository.save(content);
    }

    @Transactional
    public T update(T content) {
        content.setNew(false);
        return contentRepository.save(content);
    }

    @Transactional
    public void delete(T content) {
        delete(content.getId());
    }

    @Transactional
    public void delete(Long contentId) {
        contentRepository.deleteById(contentId);
    }

    public long count() {
        return contentRepository.count();
    }

    public List<ContentCard<T>> getCardPage(Pageable pageable) {
        return createCardList(getContentPage(pageable));
    }

    public List<ContentCard<T>> getCardPageRange(Pageable pageableFrom, Pageable pageableTo) {
        return createCardList(getContentPageRange(pageableFrom, pageableTo));
    }

    public ContentCard<T> getCardById(Long id) {
        return createCard(contentRepository.findById(id).orElse(null));
    }

    public abstract long getPage();
}

