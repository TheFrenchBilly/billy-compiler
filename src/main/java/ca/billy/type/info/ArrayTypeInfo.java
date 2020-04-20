package ca.billy.type.info;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.Type;

import ca.billy.Const;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayTypeInfo<T extends Object> implements TypeInfo<T[]> {

    private TypeInfo<T> subTypeInfo;

    private EnumType subEnumType;

    @Override
    public boolean isValidValue(String s) {
        s = s.trim();
        if (s.startsWith(Const.START_CURLY_BRACKETS) && s.endsWith(Const.END_CURLY_BRACKETS)) {
            String[] values = s.substring(1, s.length() - 1).split(Const.COMMA);

            if (!(values.length == 1 && values[0].trim().length() == 0)) {
                for (String value : values) {
                    if (!subTypeInfo.isValidValue(value.trim())) {
                        return false;
                    }
                }
            }
            return true;
        }

        return s.matches(getFilledDefaultRegex());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getDefaultValue() {
        return (T[]) Array.newInstance(subTypeInfo.getJavaClass(), 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] getValue(String value) {
        value = value.trim();

        Matcher m = Pattern.compile(getFilledDefaultRegex()).matcher(value);
        if (m.find()) {
            Integer size = Integer.parseInt(m.group(1));
            T[] values = (T[]) Array.newInstance(subTypeInfo.getJavaClass(), size);
            for (int i = 0; i < size; ++i) {
                values[i] = subTypeInfo.getDefaultValue();
            }
            return values;
        }

        String[] valuesString = value.substring(1, value.length() - 1).split(Const.COMMA);
        if (valuesString.length == 1 && valuesString[0].trim().length() == 0) {
            return getDefaultValue();
        }

        T[] values = (T[]) Array.newInstance(subTypeInfo.getJavaClass(), valuesString.length);
        for (int i = 0; i < valuesString.length; ++i) {
            values[i] = subTypeInfo.getValue(valuesString[i].trim());
        }
        return values;
    }

    @Override
    public String getName() {
        return subTypeInfo.getName() + Const.START_SQUARE_BRACKETS + Const.END_SQUARE_BRACKETS;
    }

    @Override
    public Type getBcelType() {
        return new ArrayType(subTypeInfo.getBcelType(), 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T[]> getJavaClass() {
        return (Class<T[]>) getDefaultValue().getClass();
    }

    @Override
    public void buildConst(BillyCodeInstructionArgs args, Object value) {
        Object[] values = (Object[]) value;
        TypeInfoFactory.getTypeInfo(Integer.class).buildConst(args, values.length);
        args.getIl().append(args.getFactory().createNewArray(subTypeInfo.getBcelType(), (short) 1));

        for (int i = 0; i < values.length; ++i) {
            args.getIl().append(InstructionConst.DUP);
            TypeInfoFactory.getTypeInfo(Integer.class).buildConst(args, i);
            subTypeInfo.buildConst(args, values[i]);
            args.getIl().append(InstructionFactory.createArrayStore(subTypeInfo.getBcelType()));
        }
    }

    /** Get the Bcel type of the element in the array */
    public Type getArrayBcelType() {
        return subTypeInfo.getBcelType();
    }

    /** Get the type of the element in the array */
    public EnumType getArrayType() {
        return subEnumType;
    }

    private String getFilledDefaultRegex() {
        return subTypeInfo.getName() + "\\" + Const.START_SQUARE_BRACKETS + "(\\d+)\\" + Const.END_SQUARE_BRACKETS;
    }

}
