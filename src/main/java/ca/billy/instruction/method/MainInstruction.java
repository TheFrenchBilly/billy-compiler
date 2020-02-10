package ca.billy.instruction.method;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.context.InstructionContainer;

public class MainInstruction extends MethodInstruction {

    public MainInstruction(InstructionContainer parent) {
        super(parent, main());
    }
    
    public static MethodDefinition main() {
        return new MethodDefinition("main").withArgs(new Type[] { new ArrayType(Type.STRING, 1)}).setStatic();
    }

}
