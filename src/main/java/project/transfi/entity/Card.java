package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.CardType;
import project.transfi.type.Status;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @Column(name = "cvv_hash")
    private int cvvHash;
    @ManyToOne
    @JoinColumn(name = "card_type")
    private CardCategory type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Card(BankAccount account, CardCategory type) {
        this.account = account;
        this.cardNumber = generateCardNumber();
        this.expirationDate = getExpirationDate(LocalDateTime.now());
        this.cvvHash = generateCvv();
        this.type = type;
        this.status = Status.ACTIVE;
    }

    protected Card() {
    }


    private int generateCvv() {
        return 100 + new SecureRandom().nextInt(900);
    }

    private String generateCardNumber() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private LocalDateTime getExpirationDate(LocalDateTime now) {
        return now.plusYears(4);
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
