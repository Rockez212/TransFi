package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.entity.BankAccount;
import project.transfi.entity.Transaction;
import project.transfi.repository.TransactionRepository;
import project.transfi.type.TransactionType;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void withdraw(BankAccount fromAccount, BigDecimal amount) {
        transactionRepository.save(new Transaction(fromAccount, null, TransactionType.WITHDRAW, amount));
    }

    @Transactional
    public void deposit(BankAccount fromAccount, BigDecimal amount) {
        transactionRepository.save(new Transaction(fromAccount, null, TransactionType.DEPOSIT, amount));
    }

    @Transactional
    public void transfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        transactionRepository.save(new Transaction(fromAccount, toAccount, TransactionType.TRANSFER, amount));
    }
}
