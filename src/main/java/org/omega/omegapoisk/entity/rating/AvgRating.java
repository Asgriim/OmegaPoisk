package org.omega.omegapoisk.entity.rating;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("avg_rating")
public class AvgRating {

    @Column("avg_rate")
    private double avgRate;

    @Column("content_id")
    private int contentId;

}
