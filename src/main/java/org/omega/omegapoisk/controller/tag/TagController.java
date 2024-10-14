package org.omega.omegapoisk.controller.tag;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.tag.Tag;
import org.omega.omegapoisk.service.tag.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/content/{id}")
    public ResponseEntity<?> getContentTags(@PathVariable("id") long id) {
        return ResponseEntity.ok(tagService.findByContentId(id));
    }

    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.create(tag));
    }

    @PostMapping("/{id}/content/{contId}")
    public ResponseEntity<?> addContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.addTagToContent(contId, id);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable("id") long id) {
        tagService.delete(id);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{id}/content/{contId}")
    public ResponseEntity<?> deleteContentTag(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        tagService.deleteByContentId(contId, id);
        return ResponseEntity.ok("");
    }

}
