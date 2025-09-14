package project.transfi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardDto {
    private String balance;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private String cardType;
    private String status;

}
