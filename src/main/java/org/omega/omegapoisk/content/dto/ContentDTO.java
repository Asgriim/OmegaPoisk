package org.omega.omegapoisk.content.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.omega.omegapoisk.content.entity.Content;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class ContentDTO {
    @PositiveOrZero(message = "Content ID must be positive")
    private long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
    private String description;

    public ContentDTO(Content content) {
        this.id = content.getId();
        this.title = content.getTitle();
        this.description = content.getDescription();
    }

    // Method to convert from ContentDTO to Content entity
    public abstract Content toEntity();
}
