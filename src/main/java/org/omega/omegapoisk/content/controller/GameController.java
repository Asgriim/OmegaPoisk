package org.omega.omegapoisk.content.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.content.dto.GameDTO;
import org.omega.omegapoisk.content.dto.ContentCardDTO;
import org.omega.omegapoisk.content.entity.Game;
import org.omega.omegapoisk.content.service.GameContentService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/game")
public class GameController extends AbstractContentController<Game, GameDTO> {
    private final GameContentService gameContentService;

    public GameController(GameContentService gameContentService) {
        super(gameContentService, GameDTO.class);
        this.gameContentService = gameContentService;
    }
}
