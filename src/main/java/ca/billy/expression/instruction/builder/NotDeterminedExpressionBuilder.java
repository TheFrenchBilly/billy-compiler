package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.MethodExpressionInstruction;
import ca.billy.expression.instruction.SimpleExpressionInstruction;
import ca.billy.expression.type.ExpressionType;
import ca.billy.expression.type.NotDeterminedExpressionType;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotDeterminedExpressionBuilder implements ExpressionBuilder {

    private NotDeterminedExpressionType notDeterminedExpressionType;

    @Override
    public void build(SimpleExpressionInstruction left, SimpleExpressionInstruction right, BillyCodeInstructionArgs args) {
        EnumType leftEnumType = getEnumType(left, args.getContext(), notDeterminedExpressionType.getLeft());
        EnumType rightEnumType = getEnumType(right, args.getContext(), notDeterminedExpressionType.getRight());

        retrieveBuilder(leftEnumType, rightEnumType).build(left, right, args);
    }

    @Override
    public void build(SimpleExpressionInstruction right, BillyCodeInstructionArgs args) {
        EnumType rightEnumType = getEnumType(right, args.getContext(), notDeterminedExpressionType.getRight());

        retrieveBuilder(notDeterminedExpressionType.getLeft(), rightEnumType).build(right, args);
    }

    
    // TODO FIXE THAT
    private EnumType getEnumType(SimpleExpressionInstruction exp, BillyInstructionContext context, EnumType fallBackType) {
        if (exp instanceof MethodExpressionInstruction) {
            return ((MethodExpressionInstruction) exp).getResultType();
        }
        return fallBackType;
    }

    private ExpressionBuilder retrieveBuilder(EnumType left, EnumType right) {
        ExpressionType expressionType = notDeterminedExpressionType.getOperatorEnum().retrieveExpressionType(left, right);
        notDeterminedExpressionType.setOut(expressionType.getOut());
        return expressionType.getBuilder();
    }

}
