package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.control.IfInstruction;
import ca.billy.type.EnumType;

public class IfLine extends ControlExpressionLine {

    @Override
    protected boolean isValidName(String name) {
        return name.equals(Const.IF);
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        int index = line.indexOf(Const.SPACE);
        String exp = line.substring(index + 1, line.length() - 1);

        return new IfInstruction(instructionContext, expressionProcessor.buildExpression(exp, EnumType.BOOLEAN, instructionContext));
    }
}