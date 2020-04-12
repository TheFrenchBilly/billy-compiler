package ca.billy.expression;

import ca.billy.BillyException;
import ca.billy.expression.instruction.ConstExpressionInstruction;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.type.EnumType;
import lombok.Getter;

public class Expression {

    @Getter
    private String stringExpression;

    private EnumType expectedReturn;

    private int lineNumber;

    private IExpressionInstruction expressionInstruction;

    public Expression(String stringExpression, EnumType expectedReturn) {
        this.stringExpression = stringExpression;
        this.expectedReturn = expectedReturn;
    }

    public Expression(String stringExpression, EnumType expectedReturn, int lineNumber) {
        this.stringExpression = stringExpression;
        this.expectedReturn = expectedReturn;
        this.lineNumber = lineNumber;
    }

    public Expression(EnumType type, int lineNumber) {
        this.expectedReturn = type;
        this.lineNumber = lineNumber;
        expressionInstruction = new ConstExpressionInstruction(type.getDefaultValue(), type);
    }

    public IExpressionInstruction compile(BillyCodeInstructionArgs args) {
        if (expressionInstruction != null)
            return expressionInstruction;

        try {
            return expressionInstruction = ExpressionProcessor.buildExpression(stringExpression, expectedReturn, args.getContext());
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
