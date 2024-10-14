package org.omega.omegapoisk.controller;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.studio.Studio;
import org.omega.omegapoisk.entity.tag.Tag;
import org.omega.omegapoisk.service.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {

    private final StudioService studioService;
    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studioService.getAllStudios());
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createStudio(@RequestBody Studio studio) {
        return ResponseEntity.ok(studioService.createStudio(studio));
    }

    @PostMapping("/{id}/content/{contId}")
    public ResponseEntity<?> addContentToStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        studioService.addContentToStudio(id, contId);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        studioService.deleteStudio(id);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/{id}/content/{contId}")
    public ResponseEntity<?> deleteContentStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        deleteContentStudio(id, contId);
        return ResponseEntity.ok("");
    }
}
