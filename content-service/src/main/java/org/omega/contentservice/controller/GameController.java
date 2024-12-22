package org.omega.contentservice.controller;

import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.dto.GameDTO;
import org.omega.contentservice.entity.Game;
import org.omega.contentservice.service.GameContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/game")
@CrossOrigin(originPatterns = "*")
public class GameController extends AbstractContentController<Game, GameDTO> {

    public GameController(GameContentService gameContentService, KafkaProducerService kafkaProducerService) {
        super(gameContentService, GameDTO.class, kafkaProducerService);
    }
}
