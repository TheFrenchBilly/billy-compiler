package ca.billy.line.variable;

import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableAssignementInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;
import ca.billy.util.VariableUtil;

public class VariableAssignementLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        return line.getLine().indexOf(Const.OPT_ASSIGNEMENT) != -1;
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String[] s = VariableUtil.splitAssignement(line.getLine());

        return new VariableAssignementInstruction(s[0], s[1], line.getLineNumber());
    }

}
