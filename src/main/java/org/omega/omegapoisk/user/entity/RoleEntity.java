package org.omega.omegapoisk.user.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
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
