package ca.billy.expression.instruction.node;

import ca.billy.expression.Expression;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.type.ExpressionType;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NodeLaterEvaluateExpression implements IExpressionInstruction {

    private Expression left;

    private ExpressionType expressionType;

    private Expression right;

    @Override
    public EnumType getResultType() {
        return expressionType.getOut();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expressionType.getBuilder().build(left.compile(args), right.compile(args), args);
    }

}
