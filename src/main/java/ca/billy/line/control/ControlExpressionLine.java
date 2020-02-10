package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.BillyLine;

public abstract class ControlExpressionLine implements BillyLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        if (line.endsWith(Const.COLONS)) {
            int index = line.indexOf(Const.SPACE);
            if (index != -1) {
                return isValidName(line.substring(0, index));
            }
        }

        return false;
    }
    
   
    protected abstract boolean isValidName(String name);
}
