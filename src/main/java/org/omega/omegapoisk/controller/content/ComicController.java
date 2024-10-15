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
        List<ComicDTO> comicPage = comicContentService.getComicPage(pageNumber).stream().map(ComicDTO::new).toList();
        return ResponseEntity.ok().headers(pageHeaders).body(comicPage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = comicContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, comicContentService.getPageSize(), totalCount);
        List<ContentCardDTO<ComicDTO>> cardsPage = comicContentService.getCardsPage(pageNumber).stream().map(x -> {
            ContentCardDTO<ComicDTO> card = new ContentCardDTO<>();
            card.setContent(new ComicDTO(x.getContent()));
            card.setAvgRating(x.getAvgRating());
            return card;
        }).toList();
        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Comic byId = comicContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ComicDTO(byId));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<Comic> byId = comicContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }

        ContentCardDTO<ComicDTO> card = new ContentCardDTO<>();
        card.setContent(new ComicDTO(byId.getContent()));
        card.setAvgRating(byId.getAvgRating());
        return ResponseEntity.ok(card);
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated ComicDTO comicDTO) {
        Comic resp = comicContentService.create(comicDTO.toEntity());
        return ResponseEntity.status(201).body(new ComicDTO(resp));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated ComicDTO comicDTO) {
        Comic update = comicContentService.update(comicDTO.toEntity());
        return ResponseEntity.status(201).body(new ComicDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        comicContentService.delete(id);
        return ResponseEntity.status(204).build();
    }


}
