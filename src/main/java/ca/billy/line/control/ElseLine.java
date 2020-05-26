package ca.billy.line.control;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.control.ElseInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;

public class ElseLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (line.getLine().endsWith(Const.COLONS)) {
            int index = line.getLine().indexOf(Const.SPACE);
            if (index != -1) {
                return isValidName(line.getLine().substring(0, index));
            }

            return isValidName(line.getLine().substring(0, line.getLine().indexOf(Const.COLONS)));
        }

        return false;
    }

    protected boolean isValidName(String name) {
        return name.equals(Const.ELSE);
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        if (index != -1) {
            String exp = line.getLine().substring(index + 1, line.getLine().length() - 1);
            if (!exp.trim().equals(Const.EMPTY)) {
                throw new BillyException("Invalid else ligne :" + line.getLine());
            }
        }

        return new ElseInstruction(instructionContext);
    }
}
