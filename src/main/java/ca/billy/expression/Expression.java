package ca.billy.expression;

import ca.billy.BillyException;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Expression {

    @Getter
    private String stringExpression;

    @Setter
    private EnumType expectedReturn;

    private int lineNumber;

    private IExpressionInstruction expressionInstruction;

    public IExpressionInstruction compile(BillyCodeInstructionArgs args) {
        if (expressionInstruction != null)
            return expressionInstruction;

        try {
            return expressionInstruction = ExpressionProcessor.parse(stringExpression, expectedReturn, args.getContext());
        } catch (BillyException e) {
            throw getException(e);
        }
    }

    public void build(BillyCodeInstructionArgs args) {
        IExpressionInstruction expressionInstruction = compile(args);
        try {
            expressionInstruction.build(args);
        } catch (BillyException e) {
            throw getException(e);
        }
    }

    private BillyException getException(BillyException e) {
        return lineNumber == 0 ? e : new BillyException(e.getMessage(), lineNumber, e);
    }
}
