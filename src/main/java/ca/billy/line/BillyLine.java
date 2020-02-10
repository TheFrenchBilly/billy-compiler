package ca.billy.line;

import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;

public interface BillyLine {

    public boolean isValid(String line, BillyInstructionContext instructionContext);

    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor);

}
