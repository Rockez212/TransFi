package project.transfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.transfi.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
