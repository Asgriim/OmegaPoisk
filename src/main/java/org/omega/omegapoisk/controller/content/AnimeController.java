package org.omega.omegapoisk.controller.content;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.AnimeDTO;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.service.content.AnimeContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        List<Anime> animePage = animeContentService.getAnimePage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(animePage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = animeContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, animeContentService.getPageSize(), totalCount);
        List<ContentCardDTO<Anime>> cardsPage = animeContentService.getCardsPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Anime byId = animeContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<Anime> byId = animeContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(byId);
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
