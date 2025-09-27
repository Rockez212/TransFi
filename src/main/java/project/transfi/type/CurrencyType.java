package project.transfi.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum CurrencyType implements EnumWithValue {
    USD,
    EUR,
    GBP;

    @JsonCreator
    public static CurrencyType from(String type) {
        return EnumWithValue.fromValue(CurrencyType.class, type);
    }

}
