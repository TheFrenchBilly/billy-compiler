package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.ReadLineMethodCallInstruction;

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
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        return new ReadLineMethodCallInstruction(false);
    }
}
