package org.omega.omegapoisk.entity.rating;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private long id;
    private String txt;

    @Column("user_id")
    private int userId;

    @Column("content_id")
    private int contentId;
}
