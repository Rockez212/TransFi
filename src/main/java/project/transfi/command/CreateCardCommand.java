package project.transfi.command;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.transfi.type.CardType;
import project.transfi.type.CurrencyType;

@Data
@NoArgsConstructor
public class CreateCardCommand {
    @NotNull(message = "Currency cannot be null or empty")
    private CardType cardType;
    @NotNull(message = "Currency cannot be null or empty")
    private CurrencyType currencyType;
}
