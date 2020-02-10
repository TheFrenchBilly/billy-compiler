package ca.billy.expression.instruction;

import java.util.ArrayList;
import java.util.List;

import ca.billy.expression.type.ExpressionType;
import ca.billy.expression.type.NotDeterminedExpressionType;
import ca.billy.type.EnumType;
import lombok.Getter;

public class Expression implements IExpression {

    @Getter
    private SimpleExpression left;

    private List<ExpressionType> expressionTypes;

    private List<SimpleExpression> rights;

    public Expression(SimpleExpression left) {
        this.left = left;
        expressionTypes = new ArrayList<>();
        rights = new ArrayList<>();
    }

    public void add(ExpressionType expressionType, SimpleExpression right) {
        expressionTypes.add(expressionType);
        rights.add(right);
    }

    @Override
    public EnumType getResultType() {
        if (expressionTypes.isEmpty()) {
            return left.getResultType();
        }
        return expressionTypes.get(expressionTypes.size() - 1).getOut();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expressionTypes.get(0).getBuilder().build(left, rights.get(0), args);
        for (int i = 1; i < expressionTypes.size(); ++i) {
            
            if (expressionTypes.get(i) instanceof NotDeterminedExpressionType) {
                ((NotDeterminedExpressionType) expressionTypes.get(i)).setLeft(expressionTypes.get(i - 1).getOut());
            }

            expressionTypes.get(i).getBuilder().build(rights.get(i), args);
        }
    }

}
