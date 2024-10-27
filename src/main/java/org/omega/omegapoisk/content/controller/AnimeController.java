package org.omega.omegapoisk.content.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.AnimeDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/anime")
public class AnimeController {
    private final AnimeContentService animeContentService;
    private final HeaderUtils headerUtils;

    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        long totalCount = animeContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber,animeContentService.getPageSize(), totalCount);
        List<AnimeDTO> animePage = animeContentService.getAnimePage(pageNumber).stream().map(AnimeDTO::new).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(animePage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = animeContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, animeContentService.getPageSize(), totalCount);
        List<ContentCardDTO<AnimeDTO>> cardsPage = animeContentService.getCardsPage(pageNumber).stream().map(x -> {
            ContentCardDTO<AnimeDTO> card = new ContentCardDTO<>();
            card.setContent(new AnimeDTO(x.getContent()));
            card.setAvgRating(x.getAvgRating());
            return card;
        }).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Anime byId = animeContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new AnimeDTO(byId));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<Anime> byId = animeContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        ContentCardDTO<AnimeDTO> card = new ContentCardDTO<>();
        card.setContent(new AnimeDTO(byId.getContent()));
        card.setAvgRating(byId.getAvgRating());
        return ResponseEntity.ok(card);
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated AnimeDTO anime) {
        Anime resp = animeContentService.create(anime.toEntity());
        return ResponseEntity.status(201).body(new AnimeDTO(resp));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated AnimeDTO anime) {
        Anime update = animeContentService.update(anime.toEntity());
        return ResponseEntity.status(201).body(new AnimeDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        animeContentService.delete(id);
        return ResponseEntity.status(204).build();
    }


}
