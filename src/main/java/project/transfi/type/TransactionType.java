package project.transfi.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType implements EnumWithValue {
    DEPOSIT,
    TRANSFER,
    WITHDRAW;


    @JsonCreator
    public static TransactionType from(String type) {
        return EnumWithValue.fromValue(TransactionType.class, type);
    }
}
