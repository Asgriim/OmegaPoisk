package org.omega.omegapoisk.user.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Role  {
    ADMIN("admin"),
    CREATOR("creator"),
    USER("user");

    private String string;

}
