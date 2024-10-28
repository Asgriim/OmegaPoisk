package org.omega.omegapoisk.content.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.content.entity.Comic;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComicDTO extends ContentDTO {

    @NotNull
    private boolean isColored;

    @Min(1)
    private int chaptersCount;

    // Constructor to convert from Comic entity to ComicDTO
    public ComicDTO(Comic comic) {
        super(comic);
        this.isColored = comic.isColored();
        this.chaptersCount = comic.getChaptersCount();
    }

    @Override
    public Comic toEntity() {
        Comic comic = new Comic();
        comic.setId(this.getId());
        comic.setTitle(this.getTitle());
        comic.setDescription(this.getDescription());
        comic.setColored(this.isColored);
        comic.setChaptersCount(this.chaptersCount);
        return comic;
    }
}