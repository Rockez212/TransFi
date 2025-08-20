package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.CardCategory;

import java.util.Optional;

public interface CardCategoryRepository extends JpaRepository<CardCategory, Long> {

    Optional<CardCategory> findById(Long id);
}
