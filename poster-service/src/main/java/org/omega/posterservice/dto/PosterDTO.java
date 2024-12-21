package org.omega.posterservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class PosterDTO {
    Integer contentId;
    byte[] data;
}
