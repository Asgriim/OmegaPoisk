package org.omega.omegapoisk.controller.content;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.AnimeDTO;
import org.omega.omegapoisk.dto.content.ComicDTO;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Anime;
import org.omega.omegapoisk.entity.content.Comic;
import org.omega.omegapoisk.service.content.AnimeContentService;
import org.omega.omegapoisk.service.content.ComicContentService;
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
@RequestMapping("/content/comic")
public class ComicController {
    private final ComicContentService comicContentService;
    private final HeaderUtils headerUtils;


    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        long totalCount = comicContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, comicContentService.getPageSize(), totalCount);
        List<Comic> comicPage = comicContentService.getComicPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(comicPage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = comicContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, comicContentService.getPageSize(), totalCount);
        List<ContentCardDTO<Comic>> cardsPage = comicContentService.getCardsPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(comicContentService.getById(id));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        return ResponseEntity.ok(comicContentService.getContentCardById(id));
    }

//    todo AUTH CHECK IMPL
//------------------------------
//
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated ComicDTO comicDTO) {
        Comic resp = comicContentService.create(comicDTO.toEntity());
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated ComicDTO comicDTO) {
        Comic update = comicContentService.update(comicDTO.toEntity());
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        comicContentService.delete(id);
        return ResponseEntity.ok("");
    }


}
