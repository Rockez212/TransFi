package project.transfi.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import project.transfi.command.CreateBankAccountCommand;
import project.transfi.entity.BankAccount;
import project.transfi.entity.Currency;
import project.transfi.entity.User;
import project.transfi.exception.CurrencyNotFoundException;
import project.transfi.repository.BankAccountRepository;
import project.transfi.repository.CurrencyRepository;


@RequiredArgsConstructor
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final AuthService authService;

    public void create(CreateBankAccountCommand command) {
        User currentUser = authService.getCurrentUser();
        Currency currency = currencyRepository.findById(command.getCurrencyId()).orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
        BankAccount bankAccount = new BankAccount(currentUser, currency);
        bankAccountRepository.save(bankAccount);
    }

}
