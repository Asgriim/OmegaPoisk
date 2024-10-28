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

@RestController
@RequestMapping("/content/tv-show")
public class TvShowController extends AbstractContentController<TvShow, TvShowDTO> {
    private final TvShowContentService tvShowContentService;

    public TvShowController(TvShowContentService tvShowContentService) {
        super(tvShowContentService, TvShowDTO.class);
        this.tvShowContentService = tvShowContentService;
    }
}
