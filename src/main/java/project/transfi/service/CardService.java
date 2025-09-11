package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CardDetailsCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferToCommand;
import project.transfi.entity.Card;
import project.transfi.entity.Transaction;
import project.transfi.exception.*;
import project.transfi.repository.CardCategoryRepository;
import project.transfi.repository.CardRepository;
import project.transfi.repository.CurrencyRepository;
import project.transfi.repository.TransactionRepository;
import project.transfi.type.Status;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AuthService authService;
    private final CardCategoryRepository cardCategoryRepository;
    private final CardRepository cardRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionService transactionService;
    @Value("${transfer.cross-currency-fee-percent}")
    private BigDecimal crossCurrencyFeePercent;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void create(CreateCardCommand command) {
        Card newCard = new Card(authService.getCurrentUser().getBankAccount(),
                generateCardNumber(),
                cardCategoryRepository.findById(command.getCardTypeId()).orElseThrow(() -> new CardCategoryNotFoundException("Card category not found")),
                generateCvv(),
                currencyRepository.findById(command.getCardCurrencyId()).orElseThrow(() -> new CurrencyNotFoundException("Currency not found")));
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

    @Transactional
    public void transferTo(TransferToCommand transferToCommand, CardDetailsCommand detailsCommand) {
        Card fromCard = getFromCard(transferToCommand.getCardId());
        Card toCard = getToCard(transferToCommand.getToCardNumber());

        BigDecimal amountToTransfer = prepareAmount(transferToCommand.getAmount());
        validateAmountBalance(fromCard, amountToTransfer);

        validateTransfer(fromCard, toCard, detailsCommand);
        applyTransfer(fromCard, toCard, amountToTransfer);
        Transaction transaction = transactionService.createTransaction(fromCard, toCard, transferToCommand.getTransactionType(), amountToTransfer);
        saveEntities(fromCard, toCard, transaction);

    }

    private Card getFromCard(Long fromCardId) {
        return cardRepository.findById(fromCardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private Card getToCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private BigDecimal prepareAmount(String amountToFormat) {
        BigDecimal amount = formatedBalance(amountToFormat);
        return Calculator.calculateFee(amount, crossCurrencyFeePercent);
    }

    private void validateAmountBalance(Card fromCard, BigDecimal amount) {
        if (!fromCard.checkBalance(amount)) {
            throw new NotEnoughBalanceException("Not enough balance");
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

    private boolean validateCurrency(Card fromCard, Card toCard) {
        if (fromCard.getCurrency().getCurrencyType() != toCard.getCurrency().getCurrencyType()) {
            throw new IncorrectCurrencyException("Currency is not equal to currency type");
        }
        return true;
    }

    private void validateTransfer(Card fromCard, Card toCard, CardDetailsCommand command) {
        if (!validateCard(fromCard, command) || toCard.getStatus() != Status.ACTIVE || !validateCurrency(fromCard, toCard)) {
            throw new IncorrectCredentials("Error transfer from card");
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

    private void applyTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        fromCard.subtractBalance(amount);
        toCard.addBalance(amount);
    }

    private void saveEntities(Card fromCard, Card toCard, Transaction transaction) {
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        transactionRepository.save(transaction);
    }


    @Transactional
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public BigDecimal setCrossCurrencyFeePercent(BigDecimal amount) {
        this.crossCurrencyFeePercent = amount;
        return amount;
    }


}
