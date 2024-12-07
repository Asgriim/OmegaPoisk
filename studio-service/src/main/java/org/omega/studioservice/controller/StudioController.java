package org.omega.studioservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.studioservice.dto.StudioDTO;
import org.omega.studioservice.service.StudioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${api.prefix}/studio")
@RequiredArgsConstructor
public class StudioController {

    private final StudioService studioService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(value = {"", "/"})
    public Flux<StudioDTO> getAll() {
        return studioService.getAllStudios()
                .map(StudioDTO::new);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(value = {"", "/"})
    public Mono<ResponseEntity<StudioDTO>> createStudio(@RequestBody @Validated StudioDTO studio) {
        return studioService.createStudio(studio.toEntity())
                .map(created -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new StudioDTO(created)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/{id}/content/{contId}")
    public Mono<ResponseEntity<Void>> addContentToStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        return studioService.addContentToStudio(id, contId)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") long id) {
        return studioService.deleteStudio(id)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}/content/{contId}")
    public Mono<ResponseEntity<Void>> deleteContentStudio(@PathVariable("id") long id, @PathVariable("contId") long contId) {
        return studioService.deleteContentFromStudio(id, contId)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}