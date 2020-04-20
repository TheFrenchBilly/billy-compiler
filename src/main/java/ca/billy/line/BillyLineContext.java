package ca.billy.line;

import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;

public interface BillyLineContext extends BillyLine {

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext);

}
