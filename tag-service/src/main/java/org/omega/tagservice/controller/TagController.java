package org.omega.tagservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.tagservice.dto.TagDTO;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${tag-service.api.prefix}/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/content/{id}")
    public ResponseEntity<?> getContentTags(@PathVariable("id") long id) {
        List<Tag> byId = tagService.findByContentId(id);
        return ResponseEntity.ok(byId.stream().map(TagDTO::new).toList());
    }

    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tagService.getAll().stream().map(TagDTO::new).toList());
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createTag(@RequestBody @Validated TagDTO tag) {
        Tag created = tagService.create(tag.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TagDTO(created));
    }

    @PostMapping("/{id}/content/{contId}")
    public ResponseEntity<?> addContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.addTagToContent(contId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/content/{contId}")
    public ResponseEntity<?> deleteContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.deleteByContentId(contId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
