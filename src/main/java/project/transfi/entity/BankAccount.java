package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "status")
    private Status status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "account")
    private List<Card> cards;

    public BankAccount(User user, String iban, Currency currency) {
        this.user = user;
        this.iban = iban;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.status = Status.ACTIVE;
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
