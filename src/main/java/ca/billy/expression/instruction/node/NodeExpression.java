package ca.billy.expression.instruction.node;

import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.type.ExpressionType;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NodeExpression implements IExpressionInstruction {

    private IExpressionInstruction left;

    private ExpressionType expressionType;

    private IExpressionInstruction right;

    @Override
    public EnumType getResultType() {
        return expressionType.getOut();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expressionType.getBuilder().build(left, right, args);
    }

}
