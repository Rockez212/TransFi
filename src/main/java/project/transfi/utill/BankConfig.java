package project.transfi.utill;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BankConfig {
    @Value("${bank.country-code}")
    private static String countryCode;
    @Value("${bank.account-length}")
    private static int accountLength;

    public static String getCountryCode() {
        return countryCode;
    }

    public static int getAccountLength() {
        return accountLength;
    }

}
