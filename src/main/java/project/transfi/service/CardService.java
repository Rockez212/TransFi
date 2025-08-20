package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CardDetailsCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.entity.Card;
import project.transfi.exception.CardCategoryNotFoundException;
import project.transfi.exception.CardNotFoundException;
import project.transfi.exception.IncorrectCredentials;
import project.transfi.exception.NotEnoughBalanceException;
import project.transfi.repository.CardCategoryRepository;
import project.transfi.repository.CardRepository;
import project.transfi.type.Status;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardService {


    private final AuthService authService;
    private final CardCategoryRepository cardCategoryRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void create(CreateCardCommand command) {
        Card newCard = new Card(authService.getCurrentUser().getBankAccount(), cardCategoryRepository.findById(command.getCardType()).orElseThrow(() -> new CardCategoryNotFoundException("Card category not found")));
        cardRepository.save(newCard);

    }

    @Transactional
    public void transferTo(Card fromCard, String toCardNumber, CardDetailsCommand command, int amount) {
        Card toCard = cardRepository.findByCardNumber(toCardNumber).orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!validateCard(fromCard, command) || toCard.getStatus() != Status.ACTIVE) {
            throw new IncorrectCredentials("Status of recipient is not active");
        }
        validateAmountBalance(fromCard, amount);
        fromCard.subtractBalance(BigDecimal.valueOf(amount));
        toCard.addBalance(BigDecimal.valueOf(amount));
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }

    private boolean validateCard(Card cardToValidate, CardDetailsCommand command) {
        if (cardToValidate.getStatus() == Status.ACTIVE) {
            if (cardToValidate.getCardNumber().equals(command.getCardNumber())) {
                if (cardToValidate.getExpirationDate().equals(command.getExpirationDate())) {
                    if (cardToValidate.getCvvHash() == command.getCvv()) {
                        return true;
                    } else {
                        throw new IncorrectCredentials("Incorrect cvv");
                    }
                } else {
                    throw new IncorrectCredentials("Invalid expiration date");
                }
            } else {
                throw new IncorrectCredentials("Invalid Card Number");
            }
        } else {
            throw new IncorrectCredentials("Status is not ACTIVE");
        }
    }

    private void validateAmountBalance(Card fromCard, int amount) {
        if (fromCard.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new NotEnoughBalanceException("Not enough balance");

        }
    }
}
