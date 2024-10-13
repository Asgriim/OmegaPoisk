package org.omega.omegapoisk.entity.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table("comic")
public class Comic extends Content {
    @Column("is_colored")
    private boolean isColored;

    @Column("chapters_count")
    private int chaptersCount;

}
