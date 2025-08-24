package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @SequenceGenerator(name = "transactions_id_seq", sequenceName = "transactions_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account")
    private BankAccount fromAccount;
    @Column(name = "to_account")
    private Long counterpartyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id")
    private TransactionType type;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Transaction(BankAccount fromAccount, Long counterpartyId, TransactionType type, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.counterpartyId = counterpartyId;
        this.type = type;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    protected Transaction() {
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
