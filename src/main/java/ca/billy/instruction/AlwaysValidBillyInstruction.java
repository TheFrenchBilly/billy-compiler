package ca.billy.instruction;

import ca.billy.instruction.context.BillyInstructionContext;

public interface AlwaysValidBillyInstruction extends BillyInstruction {

    @Override
    default void valid(BillyInstructionContext instructionContext) {
        
    }
}
