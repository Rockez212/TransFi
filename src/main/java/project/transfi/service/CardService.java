package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferRequest;
import project.transfi.dto.CardDto;
import project.transfi.entity.Card;
import project.transfi.exception.CardNotFoundException;
import project.transfi.mapper.CardMapper;
import project.transfi.repository.CardRepository;
import project.transfi.repository.TransactionRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AuthService authService;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional
    public void create(CreateCardCommand command) {
        Card newCard = new Card(authService.getCurrentUser().getBankAccount(), generateCardNumber(), command.getCardType(), generateCvv(), command.getCurrencyType());
        cardRepository.save(newCard);
    }

    private String generateCardNumber() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private int generateCvv() {
        return 100 + new SecureRandom().nextInt(900);
    }

    @Transactional(readOnly = true)
    public List<CardDto> getCards() {
        List<Card> cardDtos = cardRepository.findByAccount(authService.getCurrentUser().getBankAccount()).orElseThrow(() -> new CardNotFoundException("Cards not found"));
        return cardDtos.stream().map(cardMapper::toDto).toList();
    }
}
