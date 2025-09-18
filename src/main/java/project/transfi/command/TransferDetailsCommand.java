package project.transfi.command;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class TransferDetailsCommand {
    @Min(value = 1,message = "CardId cannot be zero")
    private Long cardId;
    @NotNull(message = "amount must not be null")
    private String amount;
}
