package org.omega.omegapoisk.content.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.GameDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.service.GameContentService;
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
        List<GameDTO> gamePage = gameContentService.getGamePage(pageNumber).stream().map(GameDTO::new).toList();
        
        return ResponseEntity.ok().headers(pageHeaders).body(gamePage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = gameContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, gameContentService.getPageSize(), totalCount);
        List<ContentCardDTO<GameDTO>> cardsPage = gameContentService.getCardsPage(pageNumber).stream().map(x -> {
            ContentCardDTO<GameDTO> card = new ContentCardDTO<>();
            card.setContent(new GameDTO(x.getContent()));
            card.setAvgRating(x.getAvgRating());
            return card;
        }).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Game byId = gameContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GameDTO(byId));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<Game> byId = gameContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        ContentCardDTO<GameDTO> card = new ContentCardDTO<>();
        card.setContent(new GameDTO(byId.getContent()));
        card.setAvgRating(byId.getAvgRating());
        return ResponseEntity.ok(card);
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated GameDTO GameDTO) {
        Game resp = gameContentService.create(GameDTO.toEntity());
        return ResponseEntity.status(201).body(new GameDTO(resp));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated GameDTO GameDTO) {
        Game update = gameContentService.update(GameDTO.toEntity());
        return ResponseEntity.status(201).body(new GameDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        gameContentService.delete(id);
        return ResponseEntity.status(204).build();
    }



}
