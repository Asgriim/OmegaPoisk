package org.omega.omegapoisk.utils;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.dto.ContentDTO;
import org.omega.omegapoisk.content.entity.Content;
import org.omega.omegapoisk.content.entity.ContentCard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiredArgsConstructor
public class ContentDtoEntityMapper <T extends Content, D extends ContentDTO> {

    private final Class<D> contentDtoClass;

    public D mapContentToDTO(T content) {
        try {
            Constructor<D> constructor = contentDtoClass.getConstructor(content.getClass());
            return constructor.newInstance(content);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<D> mapContentListToDTO(List<T> contentLis) {
        return contentLis.stream().map(this::mapContentToDTO).toList();
    }

    public ContentCardDTO<D> mapCardToDTO(ContentCard<T> card) {
        ContentCardDTO<D> cardDTO = new ContentCardDTO<>();
        cardDTO.setContent(mapContentToDTO(card.getContent()));
        if (card.getAvgRating() != null) {
            cardDTO.setAvgRating(card.getAvgRating().getAvgRate());
        }
        return cardDTO;
    }

    public List<ContentCardDTO<D>> mapCardListToDTO(List<ContentCard<T>> cardList) {
        return cardList.stream().map(this::mapCardToDTO).toList();
    }

}
