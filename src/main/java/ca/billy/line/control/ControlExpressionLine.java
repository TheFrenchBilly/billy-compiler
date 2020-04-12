package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;

public abstract class ControlExpressionLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (line.getLine().endsWith(Const.COLONS)) {
            int index = line.getLine().indexOf(Const.SPACE);
            if (index != -1) {
                return isValidName(line.getLine().substring(0, index));
            }
        }

        return false;
    }
    
    protected abstract boolean isValidName(String name);
    
    @Override
    public abstract VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext);
}
