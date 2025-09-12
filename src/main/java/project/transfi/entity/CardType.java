package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.CardTypeCode;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "cards_type")
public class CardType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_type_seq")
    @SequenceGenerator(name = "card_type_seq", sequenceName = "card_type_seq", allocationSize = 1)
    private Long id;
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardTypeCode cardType;


    public CardType(CardTypeCode type) {
        this.cardType = type;
    }

    protected CardType() {
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        CardType other = (CardType) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
