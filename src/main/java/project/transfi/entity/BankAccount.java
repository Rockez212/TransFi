package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.CurrencyType;
import project.transfi.type.StatusType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "iban")
    private String iban;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;
    @Column(name = "balance")
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "fromAccount",fetch = FetchType.LAZY)
    private List<Transaction> transactions;
    @OneToMany(mappedBy = "account")
    private List<Card> cards;


    public BankAccount(User user, String iban, CurrencyType currencyType) {
        this.user = user;
        this.iban = iban;
        this.currencyType = currencyType;
        this.balance = BigDecimal.ZERO;
        this.statusType = StatusType.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public BankAccount() {
    }


    public void subtractBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void addBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount acc = (BankAccount) o;
        return Objects.equals(id, acc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
