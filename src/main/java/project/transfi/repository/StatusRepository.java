package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transfi.entity.Status;
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
