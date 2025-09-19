package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account")
    private BankAccount fromAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account")
    private BankAccount toAccount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Transaction(BankAccount fromAccount, BankAccount toAccount, TransactionType transactionType, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public Transaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
