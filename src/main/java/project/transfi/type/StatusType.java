package project.transfi.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusType implements EnumWithValue {
    ACTIVE,
    FROZEN,
    CLOSED;

    @JsonCreator
    public static StatusType from(String type) {
        return EnumWithValue.fromValue(StatusType.class, type);
    }
}
