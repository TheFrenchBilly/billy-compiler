package ca.billy.expression.instruction;

import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplaceWrapperExpression implements IExpressionInstruction {

    private IExpressionInstruction expression;

    private String replaceString;

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expression.build(args);
    }

    @Override
    public EnumType getResultType() {
        return expression.getResultType();
    }
}
