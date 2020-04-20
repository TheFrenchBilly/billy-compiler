package ca.billy.type.info;

import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

class BooleanTypeInfo implements TypeInfo<Boolean> {

    @Override
    public boolean isValidValue(String s) throws BillyException {
        return s.equals(Const.TRUE) || s.equals(Const.FALSE);
    }

    @Override
    public Boolean getDefaultValue() {
        return new Boolean(false);
    }

    @Override
    public Boolean getValue(String value) {
        return Boolean.valueOf(value);
    }

    @Override
    public String getName() {
        return "Boolean";
    }

    @Override
    public Type getBcelType() {
        return Type.BOOLEAN;
    }
    
    @Override
    public Class<Boolean> getJavaClass() {
        return Boolean.class;
    }

    @Override
    public void buildConst(BillyCodeInstructionArgs args,  Object value) {
        args.getIl().append(new PUSH(args.getCp(), (boolean) value));
    }

}
