package project.transfi.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.TransferRequest;
import project.transfi.entity.Card;
import project.transfi.entity.Transaction;
import project.transfi.exception.CardNotFoundException;
import project.transfi.exception.IncorrectCredentials;
import project.transfi.exception.NotEnoughBalanceException;
import project.transfi.exception.TypeNotFoundException;
import project.transfi.repository.CardRepository;
import project.transfi.repository.TransactionRepository;
import project.transfi.type.StatusType;
import project.transfi.type.TransactionType;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    public final Calculator calculator;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
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
        transactionRepository.save(new Transaction(fromCard.getAccount(), toCard.getAccount(), TransactionType.TRANSFER, amountToSubtractWithFee));
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


    private void validateCurrency(Card fromCard, Card toCard) {
        if (fromCard.getCurrencyType() != toCard.getCurrencyType()) {
            throw new TypeNotFoundException("Currency is not equal to currency type");
        }
    }

    private void validateTransfer(Card fromCard, Card toCard, TransferRequest transferRequest) {
        fromCard.validate(transferRequest);
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
