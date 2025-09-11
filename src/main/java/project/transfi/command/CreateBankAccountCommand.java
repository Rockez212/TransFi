package project.transfi.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBankAccountCommand {
    @Min(value = 1, message = "Id must be at least 1")
    private Long currencyId;
}
