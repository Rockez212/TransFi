package project.transfi.command;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CardDetailsCommand {
    @NotBlank(message = " The card number is mandatory")
    @NotNull(message = "The card number cannot be null")
    private String toCardNumber;
    @NotNull(message = "The data cannot be null")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate expirationDate;
    @NotEmpty(message = "cvv cannot be")
    @Size(min = 3, message = "in the cvv should be minimum 3 numbers")
    @Size(max = 3, message = "in the cvv should be maximum 3 numbers")
    private int cvv;
}
