package org.omega.omegapoisk.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDTO {

    private int id;

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
    private String login;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String pass;

    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "^(admin|user|creator)$", message = "Role must be either admin or user or creator")
    private String role;
}
