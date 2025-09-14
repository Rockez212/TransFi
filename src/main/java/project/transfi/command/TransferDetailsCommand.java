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
    @Pattern(regexp = "\\d{16}",message = "Card number must be contain 16 digits")
    private String toCardNumber;
    @NotNull(message = "amount must not be null")
    private String amount;
}
