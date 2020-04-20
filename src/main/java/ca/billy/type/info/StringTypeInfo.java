package ca.billy.type.info;

import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

class StringTypeInfo implements TypeInfo<String> {

    @Override
    public boolean isValidValue(String s) throws BillyException {
        return s.startsWith(Const.QUOTE) && s.endsWith(Const.QUOTE) && (countQuote(s) == 2);
    }

    @Override
    public String getDefaultValue() {
        return "";
    }

    @Override
    public String getValue(String value) {
        return value.substring(1, value.length() - 1);
    }

    private int countQuote(String s) {
        return s.length() - s.replace(Const.QUOTE, "").length();
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public Type getBcelType() {
        return Type.STRING;
    }

    @Override
    public Class<String> getJavaClass() {
        return String.class;
    }

    @Override
    public void buildConst(BillyCodeInstructionArgs args, Object value) {
        args.getIl().append(new PUSH(args.getCp(), (String) value));
    }
}
