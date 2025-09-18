package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.CreateBankAccountCommand;
import project.transfi.entity.BankAccount;
import project.transfi.entity.User;
import project.transfi.exception.UserAlreadyHasBankAccount;
import project.transfi.repository.BankAccountRepository;
import project.transfi.utill.BankConfig;

import java.math.BigInteger;
import java.util.Random;


@RequiredArgsConstructor
@Service
public class BankAccountService {
    private final Random random = new Random();
    private final BankAccountRepository bankAccountRepository;
    private final AuthService authService;
    private final BankConfig bankConfig;

    @Transactional
    public void create(CreateBankAccountCommand command) {
        User currentUser = authService.getCurrentUser();
        checkIfBankAccountExists(currentUser);
        BankAccount bankAccount = new BankAccount(currentUser, generateIban(), command.getCurrencyType());
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
