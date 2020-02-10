package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.SimpleExpression;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

public abstract class AbstractExpressionBuilder implements ExpressionBuilder {

    @Override
    public void build(SimpleExpression left, SimpleExpression right, BillyCodeInstructionArgs args) {
        left.build(args);
        right.build(args);
        build(args);
    }

    @Override
    public void build(SimpleExpression right, BillyCodeInstructionArgs args) {
        right.build(args);
        build(args);
    }

    protected abstract void build(BillyCodeInstructionArgs args);

}
