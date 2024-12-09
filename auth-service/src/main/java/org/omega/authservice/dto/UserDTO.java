package org.omega.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.omega.authservice.entity.Role;
import org.omega.authservice.entity.User;

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
    @Pattern(regexp = "^(ADMIN|USER)$", message = "Role must be either admin or user")
    private String role;

    public User toUser() {
        return new User(id, login, pass, email, 0, null);
    }

}
