package org.omega.omegapoisk.controller.content;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.TvShowDTO;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.TvShow;
import org.omega.omegapoisk.service.content.TvShowContentService;
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
        List<TvShow> tvShowPage = tvShowContentService.getTvShowPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(tvShowPage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = tvShowContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, tvShowContentService.getPageSize(), totalCount);
        List<ContentCardDTO<TvShow>> cardsPage = tvShowContentService.getCardsPage(pageNumber);

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        return ResponseEntity.ok(tvShowContentService.getById(id));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        return ResponseEntity.ok(tvShowContentService.getContentCardById(id));
    }

//    todo AUTH CHECK IMPL
//------------------------------
//
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated TvShowDTO TvShowDTO) {
        TvShow resp = tvShowContentService.create(TvShowDTO.toEntity());
        return ResponseEntity.ok(resp);
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated TvShowDTO TvShowDTO) {
        TvShow update = tvShowContentService.update(TvShowDTO.toEntity());
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        tvShowContentService.delete(id);
        return ResponseEntity.ok("");
    }

}
