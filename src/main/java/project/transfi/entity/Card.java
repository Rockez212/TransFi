package project.transfi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.transfi.command.TransferRequest;
import project.transfi.exception.IncorrectCredentials;
import project.transfi.type.CardType;
import project.transfi.type.CurrencyType;
import project.transfi.type.StatusType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    public Card(BankAccount account, String cardNumber, CardType cardType, int cvvHash, CurrencyType currencyType) {
        this.account = account;
        this.balance = BigDecimal.ZERO;
        this.cardNumber = cardNumber;
        this.expirationDate = getExpirationDate(LocalDate.now());
        this.cvvHash = cvvHash;
        this.cardType = cardType;
        this.statusType = StatusType.ACTIVE;
        this.currencyType = currencyType;
    }

    public void validate(TransferRequest transferRequest) {
        if (getStatusType() != StatusType.ACTIVE) {
            throw new IncorrectCredentials("Status is not ACTIVE");
        }
        if (!getCardNumber().equals(transferRequest.getCardDetailsConfirmationCommand().getToCardNumber())) {
            throw new IncorrectCredentials("Invalid Card Number");
        }

        if (!getExpirationDate().equals(transferRequest.getCardDetailsConfirmationCommand().getExpirationDate())) {
            throw new IncorrectCredentials("Invalid expiration date");
        }

        if (getCvvHash() != transferRequest.getCardDetailsConfirmationCommand().getCvv()) {
            throw new IncorrectCredentials("Incorrect cvv");
        }
    }

    private LocalDate getExpirationDate(LocalDate now) {
        return now.plusYears(4);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
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
