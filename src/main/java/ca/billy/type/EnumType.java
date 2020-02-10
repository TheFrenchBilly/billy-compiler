package ca.billy.type;

import java.util.stream.Stream;

import lombok.Getter;

public enum EnumType {
    ANY(null), STRING(new StringTypeInfo()), BOOLEAN(new BooleanConverter()), INTEGER(new IntegerTypeInfo());

    @Getter
    private TypeInfo<?> typeInfo;

    EnumType(TypeInfo<?> typeInfo) {
        this.typeInfo = typeInfo;
    }

    public boolean typeMatch(Object other) {
        if (other instanceof EnumType) {
            if (this == ANY || other == ANY) {
                return true;
            }
        }

        return equals(other);
    }

    public static EnumType[] availableValues() {
        return new EnumType[] { STRING, BOOLEAN, INTEGER };
    }

    public static EnumType getEnumType(String type) {
        return Stream.of(availableValues()).filter(e -> e.typeInfo.getName().equals(type)).findFirst().orElse(null);
    }

    public static boolean anyMatch(String type) {
        return Stream.of(availableValues()).anyMatch(t -> t.typeInfo.getName().equals(type));
    }

    public static EnumType findTypeWithValue(String value) {
        return Stream.of(availableValues()).filter(t -> t.typeInfo.isValidValue(value)).findFirst().orElse(null);
    }

}
