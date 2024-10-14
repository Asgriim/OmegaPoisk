package org.omega.omegapoisk.controller.content;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.service.AnimeContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/content/anime")
public class AnimeController {
    private final AnimeContentService animeContentService;

    @GetMapping("/")
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


}
