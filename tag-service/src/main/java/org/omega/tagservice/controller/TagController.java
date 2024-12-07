package org.omega.tagservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.tagservice.dto.TagDTO;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${tag-service.api.prefix}/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/content/{id}")
    public Flux<?> getContentTags(@PathVariable("id") long id) {
        return tagService.findByContentId(id).map(TagDTO::new);
    }


    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(value = {"/",""})
    public Flux<?> getAll() {
        return tagService.getAll().map(TagDTO::new);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = {"", "/"})
    public Mono<ResponseEntity<TagDTO>> createTag(@RequestBody @Validated TagDTO tag) {
        return tagService.create(tag.toEntity())
                .map(created -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new TagDTO(created)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{id}/content/{contId}")
    public Mono<ResponseEntity<Void>> addContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        return tagService.addTagToContent(contId, id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTag(@PathVariable("id") long id) {
        return tagService.delete(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}/content/{contId}")
    public Mono<ResponseEntity<Void>> deleteContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        return tagService.deleteByContentId(contId, id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

}
