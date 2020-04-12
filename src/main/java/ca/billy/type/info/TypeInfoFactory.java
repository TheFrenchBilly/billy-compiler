package ca.billy.type.info;

import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TypeInfoFactory {

    private static StringTypeInfo stringTypeInfo = new StringTypeInfo();

    private static IntegerTypeInfo integerTypeInfo = new IntegerTypeInfo();

    private static BooleanTypeInfo booleanTypeInfo = new BooleanTypeInfo();

    private static FloatTypeInfo floaTypeInfo = new FloatTypeInfo();

    @SuppressWarnings("unchecked")
    public static <T> TypeInfo<T> getTypeInfo(Class<T> clazz) {
        if (clazz == String.class) {
            return (TypeInfo<T>) stringTypeInfo;
        }
        if (clazz == Integer.class) {
            return (TypeInfo<T>) integerTypeInfo;
        }
        if (clazz == Boolean.class) {
            return (TypeInfo<T>) booleanTypeInfo;
        }
        if (clazz == Float.class) {
            return (TypeInfo<T>) floaTypeInfo;
        }
        throw new IllegalArgumentException("No a supported type");

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> ArrayTypeInfo<T> getArrayTypeInfo(Class<T> clazz, EnumType arrayEnumType) {
        return new ArrayTypeInfo(getTypeInfo(clazz), arrayEnumType);
    }

}
