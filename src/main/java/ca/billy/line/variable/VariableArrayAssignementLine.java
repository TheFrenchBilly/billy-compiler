package ca.billy.line.variable;

import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableArrayAssignementInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.util.VariableUtil;

public class VariableArrayAssignementLine extends VariableAssignementLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (super.isValid(line, instructionContext)) {
            String[] s = VariableUtil.splitAssignement(line.getLine());
            return s[0].contains(Const.START_SQUARE_BRACKETS) && s[0].endsWith(Const.END_SQUARE_BRACKETS);
        }
        return false;
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String[] s = VariableUtil.splitAssignement(line.getLine());
        String[] arrayAccess = VariableUtil.splitArrayAccess(s[0]);

        return new VariableArrayAssignementInstruction(arrayAccess[0], arrayAccess[1], s[1], line.getLineNumber());
    }

}
