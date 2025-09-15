package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CardDetailsConfirmationCommand;
import project.transfi.command.CreateCardCommand;
import project.transfi.command.TransferDetailsCommand;
import project.transfi.dto.CardDto;
import project.transfi.dto.TransferDto;
import project.transfi.entity.Card;
import project.transfi.entity.Status;
import project.transfi.exception.*;
import project.transfi.mapper.CardMapper;
import project.transfi.repository.*;
import project.transfi.type.StatusType;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {

    private final AuthService authService;
    private final CardTypeRepository cardTypeRepository;
    private final CardRepository cardRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionService transactionService;
    private final StatusRepository statusRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CardMapper cardMapper;
    @Value("${transfer.cross-currency-fee-percent}")
    private BigDecimal crossCurrencyFeePercent;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void create(CreateCardCommand command) {
        Status status = statusRepository.findById(command.getStatusId()).orElseThrow(() -> new StatusNotFoundException("Status not found"));
        Card newCard = new Card(authService.getCurrentUser().getBankAccount(), generateCardNumber(), cardTypeRepository.findById(command.getCardTypeId()).orElseThrow(() -> new CardCategoryNotFoundException("Card category not found")), generateCvv(), status, currencyRepository.findById(command.getCardCurrencyId()).orElseThrow(() -> new CurrencyNotFoundException("Currency not found")));
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
    public void transferTo(TransferDto transferCommand) {

        Card fromCard = getFromCard(transferCommand.getTransferDetailsCommand().getCardId());
        Card toCard = getToCard(transferCommand.getTransferDetailsCommand().getToCardNumber());

        BigDecimal amountToSubtractWithFee = prepareAmount(transferCommand.getTransferDetailsCommand().getAmount());
        validateAmountBalance(fromCard, amountToSubtractWithFee);

        validateTransfer(fromCard, toCard, transferCommand.getCardDetailsConfirmationCommand(), transferCommand.getTransferDetailsCommand());
        applyTransfer(fromCard, toCard, amountToSubtractWithFee, new BigDecimal(transferCommand.getTransferDetailsCommand().getAmount()));
        saveEntities(fromCard, toCard);
    }

    private Card getFromCard(Long fromCardId) {
        return cardRepository.findById(fromCardId).orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private Card getToCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    private BigDecimal prepareAmount(String amountToFormat) {
        BigDecimal amount = formatedBalance(amountToFormat);
        return Calculator.calculateAmountAfterFee(amount, crossCurrencyFeePercent);
    }

    private void validateAmountBalance(Card fromCard, BigDecimal amountToCheck) {
        if (!fromCard.checkBalance(amountToCheck)) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
    }

    private boolean validateCard(Card fromCard, CardDetailsConfirmationCommand cardDetailsConfirmationCommand, TransferDetailsCommand transferDetailsCommand) {
        if (fromCard.getStatus().getStatusType() != StatusType.ACTIVE) {
            throw new IncorrectCredentials("Status is not ACTIVE");
        }

        if (fromCard.getCardNumber().equals(transferDetailsCommand.getToCardNumber())) {
            throw new IncorrectCredentials("Invalid Card Number");
        }

        if (!fromCard.getExpirationDate().equals(cardDetailsConfirmationCommand.getExpirationDate())) {
            throw new IncorrectCredentials("Invalid expiration date");
        }

        if (fromCard.getCvvHash() != cardDetailsConfirmationCommand.getCvv()) {
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

    private void validateTransfer(Card fromCard, Card toCard, CardDetailsConfirmationCommand cardDetailsConfirmationCommand, TransferDetailsCommand transferDetailsCommand) {
        if (!validateCard(fromCard, cardDetailsConfirmationCommand, transferDetailsCommand) || toCard.getStatus().getStatusType() != StatusType.ACTIVE || !validateCurrency(fromCard, toCard)) {
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

    private void applyTransfer(Card fromCard, Card toCard, BigDecimal amountToSubtractWithFee, BigDecimal amountToTransfer) {
        fromCard.subtractBalance(amountToSubtractWithFee);
        toCard.addBalance(amountToTransfer);
    }

    private void saveEntities(Card fromCard, Card toCard) {
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }


    @Transactional(readOnly = true)
    public List<CardDto> getCards() {
        List<Card> cardDtos = cardRepository.findByAccount(authService.getCurrentUser().getBankAccount()).orElseThrow(() -> new CardNotFoundException("Cards not found"));
        return cardDtos.stream().map(cardMapper::toDto).toList();
    }

}
