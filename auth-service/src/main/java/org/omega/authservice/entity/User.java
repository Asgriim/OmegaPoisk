package org.omega.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("user_")
public class User  {
    @Id
    private long id;
    private String login;
    private String password;
    private String email;

    @Column("role_id")
    private long roleId;

    @Transient
    private RoleEntity role;

}
