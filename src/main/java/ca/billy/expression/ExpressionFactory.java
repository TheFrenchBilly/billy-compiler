package ca.billy.expression;

import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionFactory {

    /**
     * Create a expression. Writed by the user.
     * 
     * @param stringExpression The expression {@link String}
     * @param expectedReturn The {@link EnumType} expected from the expression
     * @param lineNumber The line number
     * @return {@link Expression}
     */
    public static Expression createExpression(String stringExpression, EnumType expectedReturn, int lineNumber) {
        return new Expression(stringExpression, expectedReturn, lineNumber, null);
    }
    
    
    /**
     * Create a expression not writed by the user.
     * 
     * @param stringExpression The expression {@link String}
     * @param expectedReturn The {@link EnumType} expected from the expression
     * @return {@link Expression}
     */
    public static Expression createExpression(String stringExpression, EnumType expectedReturn) {
        return new Expression(stringExpression, expectedReturn, 0, null);
    }

    /**
     * Create a expression not writed by the user.
     * 
     * @param expressionInstruction The {@link IExpressionInstruction}
     * @return {@link Expression}
     */
    public static Expression createExpressionWithInstruction(IExpressionInstruction expressionInstruction) {
        return new Expression(null, null, 0, expressionInstruction);
    }

}
