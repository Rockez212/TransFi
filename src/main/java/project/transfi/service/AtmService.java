package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.TransferRequest;
import project.transfi.entity.Card;
import project.transfi.exception.CardNotFoundException;
import project.transfi.repository.CardRepository;
import project.transfi.repository.TransactionRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AtmService {

    private final CardRepository cardRepository;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void withdraw(TransferRequest transferRequest) {
        Card fromCard = cardRepository.findById(transferRequest.getTransferDetailsCommand().getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        BigDecimal toWithdraw = transferService.formatedBalance(transferRequest.getTransferDetailsCommand().getAmount());

        transferService.validateAmountBalance(fromCard, toWithdraw);
        transferService.validateCard(fromCard, transferRequest);

        fromCard.withdraw(toWithdraw);

        cardRepository.save(fromCard);
        transactionRepository.save(transactionService.withdraw(fromCard.getAccount(), toWithdraw));
    }

    @Transactional
    public void deposit(TransferRequest transferRequest) {
        Card toCard = cardRepository.findById(transferRequest.getTransferDetailsCommand().getCardId()).orElseThrow(() -> new CardNotFoundException("Card not found"));
        BigDecimal toDeposit = transferService.formatedBalance(transferRequest.getTransferDetailsCommand().getAmount());

        transferService.validateCard(toCard, transferRequest);
        toCard.deposit(toDeposit);

        cardRepository.save(toCard);
        transactionRepository.save(transactionService.deposit(toCard.getAccount(), toDeposit));
    }
}
