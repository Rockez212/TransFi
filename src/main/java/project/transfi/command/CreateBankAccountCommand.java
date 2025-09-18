package project.transfi.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.transfi.type.CurrencyType;

@Data
@NoArgsConstructor
public class CreateBankAccountCommand {
    @NotBlank(message = "Currency cannot be null or empty")
    private CurrencyType currencyType;
}
