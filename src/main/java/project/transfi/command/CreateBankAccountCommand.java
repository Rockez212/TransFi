package project.transfi.command;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.transfi.type.CurrencyType;

@Data
@NoArgsConstructor
public class CreateBankAccountCommand {
    @NotNull(message = "Currency cannot be null or empty")
    private CurrencyType currencyType;
}
