package org.omega.omegapoisk.content.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.TvShowDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.TvShow;
import org.omega.omegapoisk.content.service.TvShowContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/tv-show")
public class TvShowController {
    private final TvShowContentService tvShowContentService;
    private final HeaderUtils headerUtils;


    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        long totalCount = tvShowContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, tvShowContentService.getPageSize(), totalCount);
        List<TvShowDTO> tvShowPage = tvShowContentService.getTvShowPage(pageNumber).stream().map(TvShowDTO::new).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(tvShowPage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = tvShowContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, tvShowContentService.getPageSize(), totalCount);
        List<ContentCardDTO<TvShowDTO>> cardsPage = tvShowContentService.getCardsPage(pageNumber).stream().map(x -> {
            ContentCardDTO<TvShowDTO> card = new ContentCardDTO<>();
            card.setContent(new TvShowDTO(x.getContent()));
            card.setAvgRating(x.getAvgRating());
            return card;
        }).toList();


        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        TvShow byId = tvShowContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TvShowDTO(byId));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<TvShow> byId = tvShowContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        ContentCardDTO<TvShowDTO> card = new ContentCardDTO<>();
        card.setContent(new TvShowDTO(byId.getContent()));
        card.setAvgRating(byId.getAvgRating());
        return ResponseEntity.ok(card);
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated TvShowDTO TvShowDTO) {
        TvShow resp = tvShowContentService.create(TvShowDTO.toEntity());
        return ResponseEntity.status(201).body(new TvShowDTO(resp));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated TvShowDTO TvShowDTO) {
        TvShow update = tvShowContentService.update(TvShowDTO.toEntity());
        return ResponseEntity.status(201).body(new TvShowDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        tvShowContentService.delete(id);
        return ResponseEntity.status(204).build();
    }

}
