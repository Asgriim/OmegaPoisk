package org.omega.omegapoisk.entity.content;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table("comic")
public class Comic extends Content {
    @Column("is_colored")
    private boolean isColored;

    @Column("chapters_count")
    private int chaptersCount;

}
