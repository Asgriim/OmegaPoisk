package org.omega.omegapoisk.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.tag.entity.Tag;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    @PositiveOrZero(message = "Tag ID must be positive")
    private long id;

    @NotBlank(message = "Tag text cannot be blank")
    @Size(min = 1, max = 100, message = "Tag name must be between 1 and 100 characters")
    private String name;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public Tag toEntity() {
        return new Tag(this.id, this.name);
    }
}
