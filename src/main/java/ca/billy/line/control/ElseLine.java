package ca.billy.line.control;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.control.ElseInstruction;
import ca.billy.line.BillyLine;

public class ElseLine implements BillyLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        if (line.endsWith(Const.COLONS)) {
            int index = line.indexOf(Const.SPACE);
            if (index != -1) {
                return isValidName(line.substring(0, index));
            }

            return isValidName(line.substring(0, line.indexOf(Const.COLONS)));
        }

        return false;
    }

    protected boolean isValidName(String name) {
        return name.equals(Const.ELSE);
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        int index = line.indexOf(Const.SPACE);
        if (index != -1) {
            String exp = line.substring(index + 1, line.length() - 1);
            if (!exp.trim().equals(Const.EMPTY)) {
                throw new BillyException("Invalid else ligne :" + line);
            }
        }

        return new ElseInstruction(instructionContext);
    }
}
