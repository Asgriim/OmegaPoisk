package org.omega.omegapoisk.controller.content;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.GameDTO;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Game;
import org.omega.omegapoisk.service.content.GameContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/game")
public class GameController {
    private final GameContentService gameContentService;
    private final HeaderUtils headerUtils;


    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        long totalCount = gameContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, gameContentService.getPageSize(), totalCount);
        List<Game> gamePage = gameContentService.getGamePage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(gamePage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = gameContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, gameContentService.getPageSize(), totalCount);
        List<ContentCardDTO<Game>> cardsPage = gameContentService.getCardsPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(gameContentService.getById(id));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        return ResponseEntity.ok(gameContentService.getContentCardById(id));
    }

//    todo AUTH CHECK IMPL
//------------------------------
//
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated GameDTO GameDTO) {
        Game resp = gameContentService.create(GameDTO.toEntity());
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated GameDTO GameDTO) {
        Game update = gameContentService.update(GameDTO.toEntity());
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        gameContentService.delete(id);
        return ResponseEntity.ok("");
    }



}
