package ca.billy.type;

import org.apache.bcel.generic.Type;

public interface TypeInfo<T> {
    
    boolean isValidValue(String s);
        
    T getDefaultValue();
    
    T getValue(String value);
    
    Class<T> getJavaClass();
    
    String getName();
    
    Type getBcelType();
}
