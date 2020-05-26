package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.ReturnInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;

public class ReturnLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        return Const.RETURN.equals(line.getLine());
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new ReturnInstruction();
    }
}
