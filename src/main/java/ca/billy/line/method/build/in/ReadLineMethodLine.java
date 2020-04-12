package ca.billy.line.method.build.in;

import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.ReadLineMethodCallInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.line.method.AbstractParentheseLine;

public class ReadLineMethodLine extends AbstractParentheseLine {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return Const.READ_LINE.equals(methodName);
    }

    @Override
    protected int expectedNbParameter() {
        return 0;
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new ReadLineMethodCallInstruction(false);
    }
}
