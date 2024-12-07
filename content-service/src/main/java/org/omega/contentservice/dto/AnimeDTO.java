package org.omega.contentservice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omega.contentservice.entity.Anime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeDTO extends ContentDTO {
    @Min(1)
    private int seriesNum;

    // Constructor to convert from Anime entity to AnimeDTO
    public AnimeDTO(Anime anime) {
        super(anime);
        this.seriesNum = anime.getSeriesNum();
    }

    @Override
    public Anime toEntity() {
        Anime anime = new Anime();
        anime.setId(this.getId());
        anime.setTitle(this.getTitle());
        anime.setDescription(this.getDescription());
        anime.setSeriesNum(this.seriesNum);
        return anime;
    }
}