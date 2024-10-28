package org.omega.omegapoisk.content.entity;


import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table("movie")
public class Movie extends Content {
    private int duration;
}
