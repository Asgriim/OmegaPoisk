package org.omega.contentservice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omega.contentservice.entity.Movie;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO extends ContentDTO {
    @Min(1)
    private int duration;

    // Constructor to convert from Movie entity to MovieDTO
    public MovieDTO(Movie movie) {
        super(movie);
        this.duration = movie.getDuration();
    }

    @Override
    public Movie toEntity() {
        Movie movie = new Movie();
        movie.setId(this.getId());
        movie.setTitle(this.getTitle());
        movie.setDescription(this.getDescription());
        movie.setDuration(this.duration);
        return movie;
    }
}