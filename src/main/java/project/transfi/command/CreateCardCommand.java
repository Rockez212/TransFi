package project.transfi.command;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCardCommand {
    @NotBlank(message = "card type cannot be blank")
    public Long cardType;
}
