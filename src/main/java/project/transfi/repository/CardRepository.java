package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);
}
