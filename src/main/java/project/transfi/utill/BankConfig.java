package project.transfi.utill;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BankConfig {
    @Value("${bank.country.code}")
    private String countryCode;
    @Value("${bank.account.length}")
    private int accountLength;
}
