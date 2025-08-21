package project.transfi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.transfi.type.Status;
import project.transfi.utill.BankConfig;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class BankAccount {


    private static final SecureRandom random = new SecureRandom();


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "iban")
    private String iban;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "status")
    private Status status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "account")
    private List<Card> cards;
    @OneToMany(mappedBy = "fromAccount")
    private List<Transaction> transactions;


    public BankAccount(User user, Currency currency) {
        this.user = user;
        this.iban = generateIban();
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    protected BankAccount() {
    }


    private String generateIban() {
        StringBuilder accountNumber = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < BankConfig.getAccountLength(); i++) {
            accountNumber.append(chars.charAt(random.nextInt(chars.length())));
        }

        String tempIban = BankConfig.getCountryCode() + "00" + accountNumber.toString();
        String checkDigits = calculateCheckDigits(tempIban);

        return BankConfig.getCountryCode() + checkDigits + accountNumber.toString();
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

    public void subtractBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void addBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount acc = (BankAccount) o;
        return Objects.equals(id, acc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
