package ca.billy.line.control;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.LineWrapper;

public abstract class AbstractForLine extends ControlExpressionLine {

    @Override
    protected boolean isValidName(String name) {
        return "for".equals(name);
    }

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (super.isValid(line, instructionContext)) {
            int index = line.getLine().indexOf(Const.SPACE);
            String[] exps = line.getLine().substring(index + 1).split(";");
            if (exps.length == getLength()) {
                return true;
            }
        }
        return false;
    }

    protected abstract int getLength();

}
