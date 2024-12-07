package org.omega.contentservice.controller;

import org.omega.contentservice.dto.ContentCardDTO;
import org.omega.contentservice.dto.ContentDTO;
import org.omega.contentservice.entity.Content;
import org.omega.contentservice.entity.ContentCard;
import org.omega.contentservice.service.AbstractContentService;
import org.omega.contentservice.utils.ContentDtoEntityMapper;
import org.omega.contentservice.utils.HeaderUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('USER')")
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

    @PreAuthorize("hasAnyRole('USER')")
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

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        T content = contentService.getById(id);
        if (content == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(mapper.mapContentToDTO(content));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{id}/card")
    public ResponseEntity<?> getCardById(@PathVariable long id) {
        ContentCard<T> cardById = contentService.getCardById(id);
        if (cardById == null || cardById.getContent() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(mapper.mapCardToDTO(cardById));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated T content) {
        T createdContent = contentService.create(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapContentToDTO(createdContent));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated T content) {
        T updatedContent = contentService.update(content);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapContentToDTO(updatedContent));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        contentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
