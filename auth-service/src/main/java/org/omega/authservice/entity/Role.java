package org.omega.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Role  {
    ADMIN("ADMIN"),
    USER("SUER");

    private String string;

}
