package project.transfi.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferRequest {
    @Valid
    @NotNull
    private TransferDetailsCommand transferDetailsCommand;
    @Valid
    @NotNull
    private CardDetailsConfirmationCommand cardDetailsConfirmationCommand;
}
