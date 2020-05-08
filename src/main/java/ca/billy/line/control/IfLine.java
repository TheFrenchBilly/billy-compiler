package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.expression.ExpressionFactory;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.control.IfInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;

public class IfLine extends ControlExpressionLine {

    @Override
    protected boolean isValidName(String name) {
        return name.equals(Const.IF);
    }

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        String exp = line.getLine().substring(index + 1, line.getLine().length() - 1);

        return new IfInstruction(instructionContext, ExpressionFactory.createExpression(exp, EnumType.BOOLEAN, line.getLineNumber()));
    }
}
