package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CardDetailsCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferToCommand;
import project.transfi.entity.Card;
import project.transfi.exception.*;
import project.transfi.repository.CardCategoryRepository;
import project.transfi.repository.CardRepository;
import project.transfi.repository.CurrencyRepository;
import project.transfi.type.Status;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AuthService authService;
    private final CardCategoryRepository cardCategoryRepository;
    private final CardRepository cardRepository;
    private final CurrencyRepository currencyRepository;
    @Value("${transfer.cross-currency-fee-percent}")
    private BigDecimal crossCurrencyFeePercent;

    @Transactional
    public void create(CreateCardCommand command) {
        Card newCard = new Card(authService.getCurrentUser().getBankAccount(), cardCategoryRepository.findById(command.getCardTypeId()).orElseThrow(() -> new CardCategoryNotFoundException("Card category not found")), currencyRepository.findById(command.getCardCurrencyId()).orElseThrow(() -> new CurrencyNotFoundException("Currency not found")));
        cardRepository.save(newCard);

    }

    @Transactional
    public void transferTo(TransferToCommand transferToCommand, CardDetailsCommand command) {
        Card toCard = cardRepository.findByCardNumber(transferToCommand.getToCardNumber()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        Card fromCard = cardRepository.findById(transferToCommand.getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        if (!validateCard(fromCard, command) || toCard.getStatus() != Status.ACTIVE) {
            throw new IncorrectCredentials("Status of recipient is not active");
        }

        BigDecimal amountToTransfer = formatedBalance(transferToCommand.getAmount());
        amountToTransfer = calculateFee(amountToTransfer, crossCurrencyFeePercent);
        if (validateAmountBalance(fromCard, amountToTransfer)) {
            fromCard.subtractBalance(amountToTransfer);
            toCard.addBalance(amountToTransfer);
            cardRepository.save(fromCard);
            cardRepository.save(toCard);
        }
    }

    private boolean validateCard(Card fromCard, CardDetailsCommand command) {
        if (fromCard.getStatus() != Status.ACTIVE) {
            throw new IncorrectCredentials("Status is not ACTIVE");
        }

        if (!Objects.equals(fromCard.getCardNumber(), command.getToCardNumber())) {
            throw new IncorrectCredentials("Invalid Card Number");
        }

        if (!Objects.equals(fromCard.getExpirationDate(), command.getExpirationDate())) {
            throw new IncorrectCredentials("Invalid expiration date");
        }

        if (fromCard.getCvvHash() != command.getCvv()) {
            throw new IncorrectCredentials("Incorrect cvv");
        }
        return true;
    }

    private boolean validateAmountBalance(Card fromCard, BigDecimal amount) {
        if (fromCard.checkBalance(amount)) {
            return true;
        } else {
            throw new NotEnoughBalanceException("Not enough balance");
        }
    }

    private BigDecimal formatedBalance(String amountToFormat) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountToFormat);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format");
        }

        return amount;
    }

    private BigDecimal calculateFee(BigDecimal amountToTransfer, BigDecimal fee) {
        return amountToTransfer
                .multiply(fee)
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }
}
