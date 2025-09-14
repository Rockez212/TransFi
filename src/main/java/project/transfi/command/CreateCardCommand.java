package project.transfi.command;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCardCommand {
    @Min(value = 1,message = "Card Type must be at least 1")
    private Long cardTypeId;
    @Min(value = 1,message = "Card Currency must be at least 1")
    private Long cardCurrencyId;
    @Min(value = 1,message = "Status must be at least 1")
    private Long statusId;
}
