package ca.billy.line;

import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;

public interface BillyLineContext extends BillyLine {

    @Override
    public VariableInstructionContext createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor);

}
