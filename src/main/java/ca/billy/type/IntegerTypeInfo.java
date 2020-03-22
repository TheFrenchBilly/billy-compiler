package ca.billy.type;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

// TODO min, max ?
class IntegerTypeInfo implements TypeInfo<Integer> {

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
    public String getName() {
        return "Integer";
    }

    @Override
    public Type getBcelType() {
        return Type.INT;
    }
    
    @Override
    public PUSH createPush(ConstantPoolGen cp, Object value) {
        return new PUSH(cp, (int) value);
    }
}