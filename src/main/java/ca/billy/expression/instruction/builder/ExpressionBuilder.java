package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

public interface ExpressionBuilder {

    void build(IExpressionInstruction left, IExpressionInstruction right, BillyCodeInstructionArgs args);

}
