package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.CardType;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "cards_type")
public class CardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_type_seq")
    @SequenceGenerator(name = "card_type_seq", sequenceName = "card_type_seq", allocationSize = 1)
    private Long id;
    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @OneToMany(mappedBy = "type")
    private List<Card> cards;


    public CardCategory(CardType type) {
        this.cardType = type;
    }

    protected CardCategory() {
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        CardCategory other = (CardCategory) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
