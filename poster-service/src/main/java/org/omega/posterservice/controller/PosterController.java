package org.omega.posterservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.posterservice.dto.PosterDTO;
import org.omega.posterservice.entity.Poster;
import org.omega.posterservice.service.PosterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;

    @MessageMapping("/upload")
    @SendTo("/topic/status")
    public String handleImageUpload(@Payload PosterDTO posterDTO) {
        Poster poster = new Poster(null, posterDTO.getContentId(), posterDTO.getData());
        posterService.save(poster);
        return "Image uploaded successfully for content id: " +  posterDTO.getContentId();
    }

    @CrossOrigin(originPatterns = "*")
    @GetMapping("${api.prefix}/poster/{contentId}")
    @ResponseBody
    public ResponseEntity<PosterDTO> getPosterByContentId(@PathVariable Integer contentId) {
        Poster poster = posterService.getByContentId(contentId);
        if (poster != null) {
            PosterDTO posterDTO = new PosterDTO(poster.getContentId(), poster.getData());
            return ResponseEntity.ok(posterDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
