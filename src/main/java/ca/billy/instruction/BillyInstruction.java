package ca.billy.instruction;

import ca.billy.instruction.context.BillyInstructionContext;

public interface BillyInstruction {

    void valid(BillyInstructionContext instructionContext);
}
