package org.omega.contentservice.controller;

import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.dto.MovieDTO;
import org.omega.contentservice.entity.Movie;
import org.omega.contentservice.service.MovieContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/movie")
@CrossOrigin(originPatterns = "*")
public class MovieController extends AbstractContentController<Movie, MovieDTO> {

    public MovieController(MovieContentService movieContentService, KafkaProducerService kafkaProducerService) {
        super(movieContentService, MovieDTO.class, kafkaProducerService);
    }
}
