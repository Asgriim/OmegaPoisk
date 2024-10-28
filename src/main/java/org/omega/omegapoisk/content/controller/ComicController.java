package org.omega.omegapoisk.content.controller;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.ComicDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Comic;
import org.omega.omegapoisk.content.service.ComicContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/comic")
public class ComicController extends AbstractContentController<Comic, ComicDTO> {
    private final ComicContentService comicContentService;

    public ComicController(ComicContentService comicContentService) {
        super(comicContentService, ComicDTO.class);
        this.comicContentService = comicContentService;
    }
}
