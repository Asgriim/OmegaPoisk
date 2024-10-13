package org.omega.omegapoisk.entity.content;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Content {
    @Id
    private long id;
    private String title;
    private String description;
}
