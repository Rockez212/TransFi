package project.transfi.command;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CardDetailsConfirmationCommand {
    @NotNull(message = "The data cannot be null")
    @FutureOrPresent(message = "The date cannot be in the past")
    private LocalDate expirationDate;
    @NotNull(message = "CVV cannot be null")
    @Min(value = 100, message = "CVV must be at least 3 digits")
    @Max(value = 999, message = "CVV must be at most 3 digits")
    private int cvv;
}
