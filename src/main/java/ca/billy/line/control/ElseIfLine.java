package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.control.ElseIfInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;

public class ElseIfLine extends ControlExpressionLine {
    
    @Override
    protected boolean isValidName(String name) {
        return "elseif".equals(name);
    }

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        String exp = line.getLine().substring(index + 1, line.getLine().length() - 1);

        return new ElseIfInstruction(instructionContext, new Expression(exp, EnumType.BOOLEAN, line.getLineNumber()));
    }
}
