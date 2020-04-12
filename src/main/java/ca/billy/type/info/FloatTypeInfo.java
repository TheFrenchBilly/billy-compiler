package ca.billy.type.info;

import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

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
    public Class<Float> getJavaClass() {
        return Float.class;
    }

    @Override
    public void buildConst(BillyCodeInstructionArgs args, Object value) {
        args.getIl().append(new PUSH(args.getCp(), (float) value));
    }

}
