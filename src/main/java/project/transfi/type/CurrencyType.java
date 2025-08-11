package project.transfi.type;

import lombok.Getter;

@Getter
public enum CurrencyType {
    USD("$"),
    EUR("€"),
    GBP("£");

    private final String symbol;

    CurrencyType(String symbol) {
        this.symbol = symbol;
    }
}
