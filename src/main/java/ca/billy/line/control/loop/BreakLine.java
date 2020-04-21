package ca.billy.line.control.loop;

import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.control.loop.BreakInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;

public class BreakLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        return Const.BREAK.equals(line.getLine());
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new BreakInstruction();
    }
}
