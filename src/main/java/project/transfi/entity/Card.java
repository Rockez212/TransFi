package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private BankAccount account;
    @Column(name = "card_balance")
    private BigDecimal balance;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "cvv_hash")
    private int cvvHash;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type")
    private CardType type;
    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    public Card(BankAccount account, String cardNumber, CardType type, int cvvHash, Status status, Currency currency) {
        this.account = account;
        this.balance = BigDecimal.ZERO;
        this.cardNumber = cardNumber;
        this.expirationDate = getExpirationDate(LocalDate.now());
        this.cvvHash = cvvHash;
        this.type = type;
        this.status = status;
        this.currency = currency;
    }

    public Card() {
    }

    private LocalDate getExpirationDate(LocalDate now) {
        return now.plusYears(4);
    }

    public void subtractBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void addBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public boolean checkBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
