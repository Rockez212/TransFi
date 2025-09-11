package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transfi.entity.Card;
import project.transfi.entity.Transaction;
import project.transfi.exception.TransactionTypeNotFoundException;
import project.transfi.repository.TransactionTypeRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionTypeRepository transactionTypeRepository;

    public Transaction createTransaction(Card fromCard, Card toCard, Long transactionTypeId, BigDecimal amount) {
        return new Transaction(
                fromCard.getAccount(),
                toCard.getAccount(),
                transactionTypeRepository.findById(transactionTypeId).orElseThrow(() -> new TransactionTypeNotFoundException("Transaction type not found")),
                amount
        );
    }
}
