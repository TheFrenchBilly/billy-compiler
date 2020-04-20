package ca.billy.instruction.method;

import ca.billy.instruction.context.InstructionContainer;
import ca.billy.type.EnumType;

public class MainInstruction extends MethodInstruction {

    public MainInstruction(InstructionContainer parent) {
        super(parent, main());
    }

    public static MethodDefinition main() {
        return new MethodDefinition("main").withArgs(new EnumType[] { EnumType.STRING_ARRAY }).setStatic();
    }
}
