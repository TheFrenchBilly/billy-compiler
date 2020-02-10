package ca.billy.type;

import org.apache.bcel.generic.Type;

// TODO min, max ?
public class IntegerTypeInfo implements TypeInfo<Integer> {

    @Override
    public boolean isValidValue(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Integer getDefaultValue() {
        return new Integer(0);
    }

    @Override
    public Integer getValue(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Class<Integer> getJavaClass() {
        return int.class;
    }

    @Override
    public String getName() {
        return "Integer";
    }

    @Override
    public Type getBcelType() {
        return Type.INT;
    }
}
