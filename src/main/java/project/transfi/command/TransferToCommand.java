package project.transfi.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.transfi.entity.Card;

@Data
@AllArgsConstructor
public class TransferToCommand {
    private Long cardId;
    private String toCardNumber;
    private String amount;
    private Long transactionType;
}
