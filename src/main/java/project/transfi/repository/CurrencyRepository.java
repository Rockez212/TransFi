package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {

    Optional<Currency> findById(Long id);
}
