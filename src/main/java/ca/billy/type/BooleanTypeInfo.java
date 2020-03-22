package ca.billy.type;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;

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
    public PUSH createPush(ConstantPoolGen cp, Object value) {
        return new PUSH(cp, (boolean) value);
    }

}
