package org.omega.posterservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("poster")
public class Poster {
    @Id
    private Long id;

    @Column("content_id")
    private Integer contentId;

    private byte[] data;
}
