package project.transfi.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CardDetailsCommand {
    private String toCardNumber;
    private LocalDate expirationDate;
    private int cvv;
}
