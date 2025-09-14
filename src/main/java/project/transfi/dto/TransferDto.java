package project.transfi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.transfi.command.CardDetailsConfirmationCommand;
import project.transfi.command.TransferDetailsCommand;

@Data
@NoArgsConstructor
public class TransferDto {
    @Valid
    @NotNull
    private TransferDetailsCommand transferDetailsCommand;
    @Valid
    @NotNull
    private CardDetailsConfirmationCommand cardDetailsConfirmationCommand;
}
