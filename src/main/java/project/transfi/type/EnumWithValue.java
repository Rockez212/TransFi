package project.transfi.type;

import project.transfi.exception.TypeNotFoundException;
//todo: remove it and use jackson or something else
public interface EnumWithValue {

    default String getValue() {
        return ((Enum<?>) this).name();
    }

    static <E extends Enum<E> & EnumWithValue> E fromValue(Class<E> enumClass, String value) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValue().equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new TypeNotFoundException("Type not found");
    }
}
