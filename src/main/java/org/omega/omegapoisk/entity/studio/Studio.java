package org.omega.omegapoisk.entity.studio;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("studio")
public class Studio {
    @Id
    private long id;
    private String name;
}
