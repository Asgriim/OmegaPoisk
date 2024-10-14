package org.omega.omegapoisk.entity.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("role_type")
public class RoleEntity {
    @Id
    private long id;
    private String name;

    Role getRole() {
        return Role.valueOf(name.toUpperCase());
    }
}
