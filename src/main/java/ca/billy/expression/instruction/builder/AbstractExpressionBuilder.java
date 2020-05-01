package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

public abstract class AbstractExpressionBuilder implements ExpressionBuilder {

    @Override
    public void build(IExpressionInstruction left, IExpressionInstruction right, BillyCodeInstructionArgs args) {
        left.build(args);
        right.build(args);
        build(args);
    }

    protected abstract void build(BillyCodeInstructionArgs args);

}
