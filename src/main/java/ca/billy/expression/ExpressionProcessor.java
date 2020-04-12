package ca.billy.expression;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.instruction.ExpressionInstruction;
import ca.billy.expression.instruction.ExpressionInstructionFactory;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.SimpleExpressionInstruction;
import ca.billy.expression.type.ExpressionType;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

// No () so it's more easy
// No fast failing so it's more easy
public class ExpressionProcessor {

    @AllArgsConstructor
    private static class OperatorIndex {
        int index;
        OperatorEnum op;
    }
    
    public static IExpressionInstruction buildExpression(String stringExpression, EnumType expectedReturn, BillyInstructionContext instructionContext) {
        OperatorIndex opIndex = getOperatorIndex(stringExpression);
        if (opIndex.index == -1) {
            SimpleExpressionInstruction result = ExpressionInstructionFactory.createSimpleExpression(stringExpression, instructionContext);
            if (!result.matchResultType(expectedReturn)) {
                throw new BillyException(
                        "The type " + expectedReturn.getName() + " is not assignable for the type " + result.getResultType().getName());
            }
            return result;
        }

        String left = stringExpression.substring(0, opIndex.index).trim();
        String right = stringExpression.substring(opIndex.index + opIndex.op.getOperator().length()).trim();
        ExpressionInstruction result = new ExpressionInstruction(ExpressionInstructionFactory.createSimpleExpression(left, instructionContext));
        result = buildExpression(right, opIndex.op, result, expectedReturn, instructionContext);
        return result;
    }

    private static ExpressionInstruction buildExpression(String stringExpression, OperatorEnum op, ExpressionInstruction expression, EnumType expectedReturn, BillyInstructionContext instructionContext) {
        OperatorIndex opIndex = getOperatorIndex(stringExpression);
        if (opIndex.index == -1) {
            SimpleExpressionInstruction right = ExpressionInstructionFactory.createSimpleExpression(stringExpression.trim(), instructionContext);
            ExpressionType expressionType = op.retrieveExpressionType(expression.getResultType(), right.getResultType());
            if (!expressionType.getOut().typeMatch(expectedReturn)) {
                throw new BillyException(
                        "The type " + expectedReturn.getName() + " is not assignable for the type " + expressionType.getOut().getName());
            }
            expression.add(expressionType, right);
            return expression;
        }

        String left = stringExpression.substring(0, opIndex.index).trim();
        String right = stringExpression.substring(opIndex.index + opIndex.op.getOperator().length()).trim();
        SimpleExpressionInstruction leftExpression = ExpressionInstructionFactory.createSimpleExpression(left, instructionContext);
        ExpressionType expressionType = op.retrieveExpressionType(expression.getResultType(), leftExpression.getResultType());
        expression.add(expressionType, leftExpression);
        return buildExpression(right, opIndex.op, expression, expectedReturn, instructionContext);
    }

    private static OperatorIndex getOperatorIndex(String stringExpression) {
        int indexMin = stringExpression.length() + 1;
        OperatorEnum opMin = null;

        for (OperatorEnum op : OperatorEnum.values()) {
            int index = stringExpression.indexOf(op.getOperator());
            if (index != -1 && isNotInString(stringExpression, index) && index < indexMin) {
                indexMin = index;
                opMin = op;
            }
        }

        if (stringExpression.length() + 1 == indexMin) {
            indexMin = -1;
        }

        return new OperatorIndex(indexMin, opMin);
    }

    /**
     * Valid if the operator in not in a string<br>
     * TODO does it support /" ??
     */
    private static boolean isNotInString(String stringExpression, int index) {
        return stringExpression.substring(0, index).split(Const.QUOTE).length % 2 == 1;
    }

}
