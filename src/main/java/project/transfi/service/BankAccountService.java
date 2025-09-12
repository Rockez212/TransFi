package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CreateBankAccountCommand;
import project.transfi.entity.BankAccount;
import project.transfi.entity.Currency;
import project.transfi.entity.Status;
import project.transfi.entity.User;
import project.transfi.exception.CurrencyNotFoundException;
import project.transfi.exception.StatusNotFoundException;
import project.transfi.exception.UserAlreadyHasBankAccount;
import project.transfi.repository.BankAccountRepository;
import project.transfi.repository.CurrencyRepository;
import project.transfi.repository.StatusRepository;
import project.transfi.utill.BankConfig;

import java.math.BigInteger;
import java.util.Random;


@RequiredArgsConstructor
@Service
public class BankAccountService {
    private final Random random = new Random();
    private final BankAccountRepository bankAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final AuthService authService;
    private final BankConfig bankConfig;
    private final StatusRepository statusRepository;

    @Transactional
    public void create(CreateBankAccountCommand command) {
        User currentUser = authService.getCurrentUser();
        checkIfBankAccountExists(currentUser);
        Currency currency = currencyRepository.findById(command.getCurrencyId()).orElseThrow(() -> new CurrencyNotFoundException("Currency not found"));
        Status status = statusRepository.findById(command.getStatusId()).orElseThrow(() -> new StatusNotFoundException("Status not found"));
        BankAccount bankAccount = new BankAccount(currentUser, generateIban(), currency,status);
        bankAccountRepository.save(bankAccount);
    }

    private String generateIban() {
        StringBuilder accountNumber = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < bankConfig.getAccountLength(); i++) {
            accountNumber.append(chars.charAt(random.nextInt(chars.length())));
        }

        String tempIban = bankConfig.getCountryCode() + "00" + accountNumber.toString();
        String checkDigits = calculateCheckDigits(tempIban);

        return bankConfig.getCountryCode() + checkDigits + accountNumber.toString();
    }

    private String calculateCheckDigits(String iban) {
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numericIban = new StringBuilder();

        for (char ch : rearranged.toCharArray()) {
            if (Character.isDigit(ch)) {
                numericIban.append(ch);
            } else if (Character.isLetter(ch)) {
                int val = ch - 'A' + 10;
                numericIban.append(val);
            }
        }

        BigInteger ibanNum = new BigInteger(numericIban.toString());
        int mod97 = ibanNum.mod(BigInteger.valueOf(97)).intValue();
        int checkDigitValue = 98 - mod97;
        return String.format("%02d", checkDigitValue);
    }

    private void checkIfBankAccountExists(User user) {
        if (bankAccountRepository.existsByUser(user)) {
            throw new UserAlreadyHasBankAccount("User already has bank account");
        }
    }

}
