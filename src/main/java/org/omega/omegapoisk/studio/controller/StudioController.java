package org.omega.omegapoisk.studio.controller;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.studio.dto.StudioDTO;
import org.omega.omegapoisk.studio.entity.Studio;
import org.omega.omegapoisk.studio.service.StudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {

    private final StudioService studioService;
    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(studioService.getAllStudios().stream().map(StudioDTO::new).toList());
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createStudio(@RequestBody @Validated StudioDTO studio) {
        Studio created = studioService.createStudio(studio.toEntity());
        return ResponseEntity.status(201).body(new StudioDTO(created));
    }

    @PostMapping("/{id}/content/{contId}")
    public ResponseEntity<?> addContentToStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        studioService.addContentToStudio(id, contId);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        studioService.deleteStudio(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}/content/{contId}")
    public ResponseEntity<?> deleteContentStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        studioService.deleteContentFromStudio(id, contId);
        return ResponseEntity.status(204).build();
    }
}
