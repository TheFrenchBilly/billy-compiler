package ca.billy.line;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;

public interface BillyLine {

    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext);

    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext);

}
