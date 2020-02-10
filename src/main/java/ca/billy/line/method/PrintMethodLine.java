package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.PrintInstruction;
import ca.billy.type.EnumType;

public class PrintMethodLine extends AbstractParentheseLine {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return Const.PRINT.equals(methodName);
    }

    @Override
    protected int expectedNbParameter() {
        return 1;
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        return new PrintInstruction(expressionProcessor.buildExpression(extractParameters(line)[0], EnumType.ANY, instructionContext));
    }

}
