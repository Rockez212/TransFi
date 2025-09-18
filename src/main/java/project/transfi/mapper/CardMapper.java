package project.transfi.mapper;

import org.springframework.stereotype.Component;
import project.transfi.dto.CardDto;
import project.transfi.entity.Card;

import java.time.format.DateTimeFormatter;

@Component
public class CardMapper {

    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        return new CardDto(
                String.valueOf(card.getBalance()),
                card.getCardNumber(),
                card.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.valueOf(card.getCvvHash()),
                card.getCardType().name(),
                card.getStatusType().name()
        );
    }
}
