package project.transfi.command;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCardCommand {
    @NotBlank(message = "The card type cannot be blank")
    public Long cardTypeId;
    @NotBlank(message = "The card’s currency must be mandatory. ")
    public Long cardCurrencyId;
}
