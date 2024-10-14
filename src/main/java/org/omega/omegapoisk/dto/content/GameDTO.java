package org.omega.omegapoisk.dto.content;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.entity.content.Game;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO extends ContentDTO {
    @NotNull
    private boolean isFree;

    // Constructor to convert from Game entity to GameDTO
    public GameDTO(Game game) {
        super(game);
        this.isFree = game.isFree();
    }

    @Override
    public Game toEntity() {
        Game game = new Game();
        game.setId(this.getId());
        game.setTitle(this.getTitle());
        game.setDescription(this.getDescription());
        game.setFree(this.isFree);
        return game;
    }
}