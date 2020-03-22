package ca.billy.type;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

class FloatTypeInfo implements TypeInfo<Float> {

    @Override
    public boolean isValidValue(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException value) {
            return false;
        }
    }

    @Override
    public Float getDefaultValue() {
        return new Float(0);
    }

    @Override
    public Float getValue(String value) {
        return Float.parseFloat(value);
    }

    @Override
    public String getName() {
        return "Float";
    }

    @Override
    public Type getBcelType() {
        return Type.FLOAT;
    }
    
    @Override
    public PUSH createPush(ConstantPoolGen cp, Object value) {
        return new PUSH(cp, (float) value);
    }

}
