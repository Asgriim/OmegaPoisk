package org.omega.omegapoisk.dto.studio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.entity.studio.Studio;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudioDTO {
    @PositiveOrZero(message = "Studio ID must be positive")
    private long id;

    @NotBlank(message = "Studio text cannot be blank")
    @Size(min = 1, max = 100, message = "Studio name must be between 1 and 100 characters")
    private String name;

    public StudioDTO(Studio studio) {
        this.id = studio.getId();
        this.name = studio.getName();
    }

    public Studio toEntity() {
        return new Studio(this.id, this.name);
    }
}
