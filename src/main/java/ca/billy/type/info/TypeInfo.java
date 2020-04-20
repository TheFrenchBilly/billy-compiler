package ca.billy.type.info;

import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

/**
 * Class to describe a billy type.
 * 
 * @author cedric.bilodeau
 *
 * @param <T> The JAVA type
 */
public interface TypeInfo<T> {
    
    boolean isValidValue(String s);
        
    T getDefaultValue();
    
    T getValue(String value);
       
    String getName();
    
    Type getBcelType();
    
    Class<T> getJavaClass();
    
    void buildConst(BillyCodeInstructionArgs args, Object value);
}
