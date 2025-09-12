package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
