package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CardDetailsCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferToCommand;
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
    public void transferTo(TransferToCommand transferToCommand, CardDetailsCommand command) {
        Card toCard = cardRepository.findByCardNumber(transferToCommand.getToCardNumber()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        Card fromCard = cardRepository.findById(transferToCommand.getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        if (!validateCard(fromCard, command) || toCard.getStatus() != Status.ACTIVE) {
            throw new IncorrectCredentials("Status of recipient is not active");
        }

        validateAmountBalance(fromCard, transferToCommand.getAmount());
        fromCard.subtractBalance(BigDecimal.valueOf(transferToCommand.getAmount()));
        toCard.addBalance(BigDecimal.valueOf(transferToCommand.getAmount()));
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }

    private boolean validateCard(Card cardToValidate, CardDetailsCommand command) {
        if (cardToValidate.getStatus() == Status.ACTIVE) {
            if (cardToValidate.getCardNumber().equals(command.getToCardNumber())) {
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
