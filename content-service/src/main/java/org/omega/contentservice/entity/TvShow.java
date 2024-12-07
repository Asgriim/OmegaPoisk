package org.omega.contentservice.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table("tv_show")
public class TvShow extends Content {
    @Column("series_num")
    int seriesNum;
}
