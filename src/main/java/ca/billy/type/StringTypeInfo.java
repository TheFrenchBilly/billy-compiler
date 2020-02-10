package ca.billy.type;

import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;

public class StringTypeInfo implements TypeInfo<String> {

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
        return value.substring(1, value.length() -1);
    }

    private int countQuote(String s) {
        return s.length() - s.replace(Const.QUOTE, "").length();
    }

    @Override
    public Class<String> getJavaClass() {
        return String.class;
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public Type getBcelType() {
        return Type.STRING;
    }
}
