package ca.billy.line.control.loop;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.control.loop.WhileInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.line.control.ControlExpressionLine;
import ca.billy.type.EnumType;

public class WhileLine extends ControlExpressionLine {

    @Override
    protected boolean isValidName(String name) {
        return "while".equals(name);
    }

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        String exp = line.getLine().substring(index + 1, line.getLine().length() - 1);

        return new WhileInstruction(instructionContext, new Expression(exp, EnumType.BOOLEAN, line.getLineNumber()));
    }

}
