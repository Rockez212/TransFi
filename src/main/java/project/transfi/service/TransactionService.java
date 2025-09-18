package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.transfi.entity.BankAccount;
import project.transfi.entity.Transaction;
import project.transfi.type.TransactionType;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {


    public Transaction withdraw(BankAccount fromAccount, BigDecimal amount) {
        return new Transaction(fromAccount, null, TransactionType.WITHDRAW, amount);
    }

    public Transaction deposit(BankAccount fromAccount, BigDecimal amount) {
        return new Transaction(fromAccount, null, TransactionType.DEPOSIT, amount);
    }

    public Transaction transfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        return new Transaction(fromAccount, toAccount, TransactionType.TRANSFER, amount);
    }
}
