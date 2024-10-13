package org.omega.omegapoisk.entity.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Role implements GrantedAuthority {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user");

    private String string;

    @Override
    public String getAuthority() {
        return string;
    }
}
