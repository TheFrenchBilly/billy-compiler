package ca.billy.type;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

/**
 * Class to describe a billy type.
 * 
 * @author cedric.bilodeau
 *
 * @param <T> The JAVA type
 */
interface TypeInfo<T> {
    
    boolean isValidValue(String s);
        
    T getDefaultValue();
    
    T getValue(String value);
       
    String getName();
    
    Type getBcelType();
    
    PUSH createPush(ConstantPoolGen cp, Object value);
}
