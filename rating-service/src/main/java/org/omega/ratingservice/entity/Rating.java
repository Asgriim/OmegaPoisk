package org.omega.ratingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    private long id;
    private int value;

    @Column("user_id")
    private int userId;

    @Column("content_id")
    private int contentId;
}
