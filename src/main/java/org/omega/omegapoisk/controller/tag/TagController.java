package org.omega.omegapoisk.controller.tag;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.tag.TagDTO;
import org.omega.omegapoisk.entity.tag.Tag;
import org.omega.omegapoisk.service.tag.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
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
        return ResponseEntity.status(201).body(new TagDTO(created));
    }

    @PostMapping("/{id}/content/{contId}")
    public ResponseEntity<?> addContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.addTagToContent(contId, id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}/content/{contId}")
    public ResponseEntity<?> deleteContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.deleteByContentId(contId, id);
        return ResponseEntity.status(204).build();
    }

}
