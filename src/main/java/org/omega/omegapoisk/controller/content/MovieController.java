package org.omega.omegapoisk.controller.content;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.content.MovieDTO;
import org.omega.omegapoisk.dto.content.MovieDTO;
import org.omega.omegapoisk.dto.content.ContentCardDTO;
import org.omega.omegapoisk.entity.content.Movie;
import org.omega.omegapoisk.entity.content.Movie;
import org.omega.omegapoisk.service.content.MovieContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content/movie")
public class MovieController {
    private final MovieContentService movieContentService;
    private final HeaderUtils headerUtils;


    @GetMapping(value = {"","/"})
    public ResponseEntity<?> getPage(@RequestParam("page") int pageNumber) {
        long totalCount = movieContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, movieContentService.getPageSize(), totalCount);
        List<MovieDTO> moviePage = movieContentService.getMoviePage(pageNumber).stream().map(MovieDTO::new).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(moviePage);
    }

    @GetMapping("/card")
    public ResponseEntity<?> getCardPage(@RequestParam("page") int pageNumber) {
        long totalCount = movieContentService.countAll();
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber, movieContentService.getPageSize(), totalCount);
        List<ContentCardDTO<MovieDTO>> cardsPage = movieContentService.getCardsPage(pageNumber).stream().map(x -> {
            ContentCardDTO<MovieDTO> card = new ContentCardDTO<>();
            card.setContent(new MovieDTO(x.getContent()));
            card.setAvgRating(x.getAvgRating());
            return card;
        }).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(cardsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Movie byId = movieContentService.getById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MovieDTO(byId));
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<?> getByCardById(@PathVariable long id) {
        ContentCardDTO<Movie> byId = movieContentService.getContentCardById(id);
        if (byId == null) {
            return ResponseEntity.notFound().build();
        }
        ContentCardDTO<MovieDTO> card = new ContentCardDTO<>();
        card.setContent(new MovieDTO(byId.getContent()));
        card.setAvgRating(byId.getAvgRating());
        return ResponseEntity.ok(card);
    }


    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated MovieDTO MovieDTO) {
        Movie resp = movieContentService.create(MovieDTO.toEntity());
        return ResponseEntity.status(201).body(new MovieDTO(resp));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated MovieDTO MovieDTO) {
        Movie update = movieContentService.update(MovieDTO.toEntity());
        return ResponseEntity.status(201).body(new MovieDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        movieContentService.delete(id);
        return ResponseEntity.status(204).build();
    }
}
