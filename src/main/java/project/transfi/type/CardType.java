package project.transfi.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CardType implements EnumWithValue {
    DEBIT,
    CREDIT;

    @JsonCreator
    public static CardType from(String type) {
        return EnumWithValue.fromValue(CardType.class, type);
    }
}
