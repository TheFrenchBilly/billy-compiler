package ca.billy.type.info;

import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

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
    public Class<Integer> getJavaClass() {
        return Integer.class;
    }

    @Override
    public void buildConst(BillyCodeInstructionArgs args, Object value) {
        args.getIl().append(new PUSH(args.getCp(), (int) value));
    }
}
