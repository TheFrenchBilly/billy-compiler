package ca.billy.line.method;

import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.StaticMethodCallInstruction;
import ca.billy.line.BillyLine;

public class StaticMethodCallLine extends AbstractMethodWithoutParameterLine implements BillyLine {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return true;
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        return new StaticMethodCallInstruction(extractMethodName(line));
    }

}
