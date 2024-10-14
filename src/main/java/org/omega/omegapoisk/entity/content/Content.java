package org.omega.omegapoisk.entity.content;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Content implements Persistable<Long> {
    @Id
    private Long id;
    private String title;
    private String description;

    @Transient
    boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
