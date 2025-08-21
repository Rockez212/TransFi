package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.TransactionTypeCode;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "transactions_type")
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_type_id_seq")
    @SequenceGenerator(name = "transaction_type_id_seq", sequenceName = "transaction_type_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private TransactionTypeCode name;



    public TransactionType(TransactionTypeCode name) {
        this.name = name;
    }

    protected TransactionType() {
    }

    @Override
    public boolean equals(Object o) {
        if (o != null || getClass() != o.getClass()) return false;
        TransactionType that = (TransactionType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
