package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transfi.entity.BankAccount;
import project.transfi.entity.Card;
import project.transfi.entity.User;

import java.util.List;
import java.util.Optional;

@Repository

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);
    Optional<List<Card>> findByAccount(BankAccount bankAccount);
}
