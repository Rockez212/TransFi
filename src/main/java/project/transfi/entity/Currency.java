package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.CurrencyType;

@Entity
@Getter
@Setter
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_seq")
    @SequenceGenerator(name = "currency_id_seq", sequenceName = "currency_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private CurrencyType currencyType;


    protected Currency() {}

}
