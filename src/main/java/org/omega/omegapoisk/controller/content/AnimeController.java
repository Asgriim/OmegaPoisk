package org.omega.omegapoisk.controller.content;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.AnimeDTO;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.service.AnimeContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/anime")
public class AnimeController {
    private final AnimeContentService animeContentService;

    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(animeContentService.getAll());
    }

    @GetMapping("/card")
    public ResponseEntity<?> getAllCards() {
        return ResponseEntity.ok(animeContentService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(animeContentService.getById(id));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        return ResponseEntity.ok(animeContentService.getContentCardById(id));
    }

    @GetMapping("?page")
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        return ResponseEntity.ok(animeContentService.getAnimePage(pageNumber));
    }

    @GetMapping("/card/?page")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        return ResponseEntity.ok(animeContentService.getCardsPage(pageNumber));
    }

//    todo AUTH CHECK IMPL
//------------------------------
//
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated AnimeDTO anime) {
        Anime resp = animeContentService.create(anime.toEntity());
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated AnimeDTO anime) {
        Anime update = animeContentService.update(anime.toEntity());
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        animeContentService.delete(id);
        return ResponseEntity.ok("");
    }


}
