package project.transfi.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBankAccountCommand {
    private Long userId;
    private Long currencyId;
}
