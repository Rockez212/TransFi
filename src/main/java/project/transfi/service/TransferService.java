package project.transfi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.TransferRequest;
import project.transfi.entity.Card;
import project.transfi.exception.CardNotFoundException;
import project.transfi.exception.IncorrectCredentials;
import project.transfi.exception.NotEnoughBalanceException;
import project.transfi.exception.TypeNotFoundException;
import project.transfi.repository.CardRepository;
import project.transfi.repository.TransactionRepository;
import project.transfi.type.StatusType;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    public final Calculator calculator;
    private final CardRepository cardRepository;
    private final TransactionService transactionService;
    @Value("${transfer.cross-currency-fee-percent}")
    private BigDecimal crossCurrencyFeePercent;

    @Transactional
    public void transferTo(TransferRequest transferRequest) {
        Card fromCard = cardRepository.findById(transferRequest.getTransferDetailsCommand().getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        Card toCard = cardRepository.findByCardNumber(transferRequest.getCardDetailsConfirmationCommand().getToCardNumber()).orElseThrow(() -> new CardNotFoundException("Card not found"));

        BigDecimal amountToSubtractWithFee = prepareAmount(transferRequest.getTransferDetailsCommand().getAmount());

        validateAmountBalance(fromCard, amountToSubtractWithFee);
        validateTransfer(fromCard, toCard, transferRequest);
        applyTransfer(fromCard, toCard, amountToSubtractWithFee, new BigDecimal(transferRequest.getTransferDetailsCommand().getAmount()));

        cardRepository.saveAll(List.of(fromCard, toCard));
        transactionService.transfer(fromCard.getAccount(), toCard.getAccount(), amountToSubtractWithFee);
    }

    private BigDecimal prepareAmount(String amountToFormat) {
        BigDecimal amount = formatedBalance(amountToFormat);
        return calculator.calculateAmountAfterFee(amount, crossCurrencyFeePercent);
    }

    public void validateAmountBalance(Card fromCard, BigDecimal amountToCheck) {
        if (!fromCard.checkBalance(amountToCheck)) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
    }

    //todo: read about rich model and remove this one!
    public void validateCard(Card fromCard, TransferRequest transferRequest) {
        if (fromCard.getStatusType() != StatusType.ACTIVE) {
            throw new IncorrectCredentials("Status is not ACTIVE");
        }
        if (!fromCard.getCardNumber().equals(transferRequest.getCardDetailsConfirmationCommand().getToCardNumber())) {
            throw new IncorrectCredentials("Invalid Card Number");
        }

        if (!fromCard.getExpirationDate().equals(transferRequest.getCardDetailsConfirmationCommand().getExpirationDate())) {
            throw new IncorrectCredentials("Invalid expiration date");
        }

        if (fromCard.getCvvHash() != transferRequest.getCardDetailsConfirmationCommand().getCvv()) {
            throw new IncorrectCredentials("Incorrect cvv");
        }
    }

    private void validateCurrency(Card fromCard, Card toCard) {
        if (fromCard.getCurrencyType() != toCard.getCurrencyType()) {
            throw new TypeNotFoundException("Currency is not equal to currency type");
        }
    }

    private void validateTransfer(Card fromCard, Card toCard, TransferRequest transferRequest) {
        validateCard(fromCard, transferRequest);
        if (toCard.getStatusType() != StatusType.ACTIVE) {
            throw new IncorrectCredentials("Error transfer from card");
        }
        validateCurrency(fromCard, toCard);
    }

    public BigDecimal formatedBalance(String amountToFormat) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountToFormat);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format");
        }

        return amount;
    }

    private void applyTransfer(Card fromCard, Card toCard, BigDecimal amountToSubtractWithFee, BigDecimal amountToTransfer) {
        fromCard.withdraw(amountToSubtractWithFee);
        toCard.deposit(amountToTransfer);
    }
}
