package org.omega.omegapoisk.content.controller;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.MovieDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Movie;
import org.omega.omegapoisk.content.service.MovieContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/movie")
public class MovieController extends AbstractContentController<Movie, MovieDTO> {
    private final MovieContentService movieContentService;

    public MovieController(MovieContentService movieContentService) {
        super(movieContentService, MovieDTO.class);
        this.movieContentService = movieContentService;
    }

}
