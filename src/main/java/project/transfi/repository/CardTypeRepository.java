package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transfi.entity.CardType;

import java.util.Optional;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Long> {

    Optional<CardType> findById(Long id);
}
