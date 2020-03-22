package ca.billy.type;

import java.util.stream.Stream;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

public enum EnumType {
    ANY(null), STRING(new StringTypeInfo()), BOOLEAN(new BooleanTypeInfo()), INTEGER(new IntegerTypeInfo()), FLOAT(new FloatTypeInfo());

    private TypeInfo<?> typeInfo;

    EnumType(TypeInfo<?> typeInfo) {
        this.typeInfo = typeInfo;
    }

    public Object getDefaultValue() {
        return typeInfo.getDefaultValue();
    }

    public Object getValue(String value) {
        return typeInfo.getValue(value);
    }

    public String getName() {
        return typeInfo.getName();
    }

    public Type getBcelType() {
        return typeInfo.getBcelType();
    }
    
    public PUSH createPush(ConstantPoolGen cp, Object value) {
        return typeInfo.createPush(cp, value);
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
        return new EnumType[] { STRING, BOOLEAN, INTEGER, FLOAT };
    }

    public static EnumType[] availableNumerics() {
        return new EnumType[] { INTEGER, FLOAT };
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
