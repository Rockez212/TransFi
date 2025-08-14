package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
}
