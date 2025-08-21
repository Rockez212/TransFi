package project.transfi.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpCommand {
    @NotBlank(message = "username cannot be empty")
    private String username;
    @Email(message = "Incorrect email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 8, max = 18, message = "The password must be 8-18 characters long")
    private String password;
}
