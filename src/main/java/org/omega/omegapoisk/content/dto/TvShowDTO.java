package org.omega.omegapoisk.content.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.content.entity.TvShow;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TvShowDTO extends ContentDTO {
    @Min(1)
    private int seriesNum;

    // Constructor to convert from TvShow entity to TvShowDTO
    public TvShowDTO(TvShow tvShow) {
        super(tvShow);
        this.seriesNum = tvShow.getSeriesNum();
    }

    @Override
    public TvShow toEntity() {
        TvShow tvShow = new TvShow();
        tvShow.setId(this.getId());
        tvShow.setTitle(this.getTitle());
        tvShow.setDescription(this.getDescription());
        tvShow.setSeriesNum(this.seriesNum);
        return tvShow;
    }
}