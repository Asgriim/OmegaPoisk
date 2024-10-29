package org.omega.omegapoisk.content.controller;

import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.dto.ContentDTO;
import org.omega.omegapoisk.content.entity.Content;
import org.omega.omegapoisk.content.entity.ContentCard;
import org.omega.omegapoisk.content.service.AbstractContentService;
import org.omega.omegapoisk.utils.ContentDtoEntityMapper;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AbstractContentController <T extends Content, DTO extends ContentDTO> {
    private final AbstractContentService<T> contentService;
    private final ContentDtoEntityMapper<T, DTO> mapper;

    public AbstractContentController(AbstractContentService<T> contentService, Class<DTO> dtoClass) {
        this.contentService = contentService;
        this.mapper = new ContentDtoEntityMapper<>(dtoClass);
    }


    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getContentRange(@RequestParam(required = false) Integer page, @RequestParam(required = false, name = "page-to") Integer pageTo) {
        long totalCount = contentService.count();

        if (page == null) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, (int) contentService.getPage());
        HttpHeaders pageHeaders = HeaderUtils.createPageHeaders(pageable, totalCount);
        List<T> content;

        if (pageTo != null) {
            Pageable pageableTo = PageRequest.of(pageTo, (int) contentService.getPage());
            content = contentService.getContentPageRange(pageable, pageableTo);
        } else {
            content = contentService.getContentPage(pageable);
        }

        List<DTO> contentPage = mapper.mapContentListToDTO(content);

        return ResponseEntity.ok().headers(pageHeaders).body(contentPage);
    }


    @GetMapping("/card")
    public ResponseEntity<?> getCardRange(@RequestParam(required = false) Integer page, @RequestParam(required = false, name = "page-to") Integer pageTo) {
        long totalCount = contentService.count();

        if (page == null) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, (int) contentService.getPage());
        HttpHeaders pageHeaders = HeaderUtils.createPageHeaders(pageable, totalCount);
        List<ContentCard<T>> cards;

        if (pageTo != null) {
            Pageable pageableTo = PageRequest.of(pageTo, (int) contentService.getPage());
            cards = contentService.getCardPageRange(pageable, pageableTo);
        } else {
            cards = contentService.getCardPage(pageable);
        }

        List<ContentCardDTO<DTO>> cardsDTO = mapper.mapCardListToDTO(cards);

        return ResponseEntity.ok().headers(pageHeaders).body(cardsDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        T content = contentService.getById(id);
        if (content == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(mapper.mapContentToDTO(content));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getCardById(@PathVariable long id) {
        ContentCard<T> cardById = contentService.getCardById(id);
        if (cardById == null || cardById.getContent() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(mapper.mapCardToDTO(cardById));
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated T content) {
        T createdContent = contentService.create(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapContentToDTO(createdContent));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated T content) {
        T updatedContent = contentService.update(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapContentToDTO(updatedContent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        contentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
