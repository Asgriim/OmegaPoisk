package org.omega.omegapoisk.content.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.AnimeDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Anime;
import org.omega.omegapoisk.content.service.AbstractContentService;
import org.omega.omegapoisk.content.service.AnimeContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/anime")
public class AnimeController extends AbstractContentController<Anime, AnimeDTO> {
    private final AnimeContentService animeContentService;

    public AnimeController(AnimeContentService animeContentService) {
        super(animeContentService, AnimeDTO.class);
        this.animeContentService = animeContentService;
    }
}