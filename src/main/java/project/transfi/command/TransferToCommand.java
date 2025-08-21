package project.transfi.command;

import lombok.Data;
import project.transfi.entity.Card;

@Data
public class TransferToCommand {
    private Long cardId;
    private String toCardNumber;
    private int amount;
}
