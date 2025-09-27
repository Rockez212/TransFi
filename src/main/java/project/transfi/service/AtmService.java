package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.TransferRequest;
import project.transfi.entity.Card;
import project.transfi.entity.Transaction;
import project.transfi.exception.CardNotFoundException;
import project.transfi.repository.CardRepository;
import project.transfi.repository.TransactionRepository;
import project.transfi.type.TransactionType;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final CardRepository cardRepository;
    private final TransferService transferService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void withdraw(TransferRequest transferRequest) {
        Card fromCard = cardRepository.findById(transferRequest.getTransferDetailsCommand().getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        BigDecimal toWithdraw = transferService.formatedBalance(transferRequest.getTransferDetailsCommand().getAmount());
        fromCard.validate(transferRequest);
        fromCard.validate(transferRequest);
        transferService.validateAmountBalance(fromCard, toWithdraw);

        fromCard.withdraw(toWithdraw);

        cardRepository.save(fromCard);
        transactionRepository.save(new Transaction(fromCard.getAccount(), null, TransactionType.WITHDRAW, toWithdraw));
    }

    @Transactional
    public void deposit(TransferRequest transferRequest) {
        Card toCard = cardRepository.findById(transferRequest.getTransferDetailsCommand().getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        BigDecimal toDeposit = transferService.formatedBalance(transferRequest.getTransferDetailsCommand().getAmount());

        toCard.validate(transferRequest);
        toCard.deposit(toDeposit);

        transactionRepository.save(new Transaction(toCard.getAccount(), null, TransactionType.DEPOSIT, toDeposit));
    }
}
