package ca.billy.type;

import static ca.billy.type.info.TypeInfoFactory.getArrayTypeInfo;
import static ca.billy.type.info.TypeInfoFactory.getTypeInfo;

import java.util.stream.Stream;

import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.type.info.ArrayTypeInfo;
import ca.billy.type.info.TypeInfo;

public enum EnumType {
    // @formatter:off
    ANY(null),
    STRING(getTypeInfo(String.class)),
    BOOLEAN(getTypeInfo(Boolean.class)),
    INTEGER(getTypeInfo(Integer.class)),
    FLOAT(getTypeInfo(Float.class)),
    ANY_ARRAY(null),
    STRING_ARRAY(getArrayTypeInfo(String.class, STRING)),
    BOOLEAN_ARRAY(getArrayTypeInfo(Boolean.class, BOOLEAN)),
    INTEGER_ARRAY(getArrayTypeInfo(Integer.class, INTEGER)),
    FLOAT_ARRAY(getArrayTypeInfo(Float.class, FLOAT));
    // @formatter:on

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

    public void buildConst(BillyCodeInstructionArgs args, Object value) {
        typeInfo.buildConst(args, value);
    }

    public boolean typeMatch(Object other) {
        if (other instanceof EnumType) {
            if (this == ANY  || other == ANY) {
                return true;
            }

            if ((this == ANY_ARRAY && ((EnumType) other).isArray()) || (other == ANY_ARRAY && this.isArray())) {
                return true;
            }
        }

        return equals(other);
    }

    public boolean isArray() {
        return typeInfo instanceof ArrayTypeInfo;
    }

    public Type getArrayBcelType() {
        return ((ArrayTypeInfo<?>) typeInfo).getArrayBcelType();
    }

    public EnumType getArrayType() {
        return ((ArrayTypeInfo<?>) typeInfo).getArrayType();
    }

    public static EnumType[] availableValues() {
        return new EnumType[] { STRING, BOOLEAN, INTEGER, FLOAT, STRING_ARRAY, BOOLEAN_ARRAY, INTEGER_ARRAY, FLOAT_ARRAY };
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
