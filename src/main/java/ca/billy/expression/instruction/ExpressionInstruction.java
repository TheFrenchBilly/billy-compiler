package ca.billy.expression.instruction;

import java.util.ArrayList;
import java.util.List;

import ca.billy.expression.type.ExpressionType;
import ca.billy.type.EnumType;
import lombok.Getter;

public class ExpressionInstruction implements IExpressionInstruction {

    @Getter
    private SimpleExpressionInstruction left;

    private List<ExpressionType> expressionTypes;

    private List<SimpleExpressionInstruction> rights;

    public ExpressionInstruction(SimpleExpressionInstruction left) {
        this.left = left;
        expressionTypes = new ArrayList<>();
        rights = new ArrayList<>();
    }

    public void add(ExpressionType expressionType, SimpleExpressionInstruction right) {
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
            expressionTypes.get(i).getBuilder().build(rights.get(i), args);
        }
    }

}
