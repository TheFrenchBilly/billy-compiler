package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;
import ca.billy.util.MethodUtil;

// TODO MODIFY for parameters ! 
// Expression in parameter ?
public abstract class AbstractParentheseLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.START_PARENTHESES);

        if (index == -1 || !line.getLine().endsWith(Const.END_PARENTHESES))
            return false;

        return isValidMethodName(line.getLine().substring(0, index)) && isValidParameters(MethodUtil.extractParameters(line.getLine()), instructionContext);
    }

    private boolean isValidParameters(String[] parameters, BillyInstructionContext instructionContext) {
        if (parameters.length != expectedNbParameter())
            return false;

        // TODO validation ?
        return true;
    }

    protected abstract boolean isValidMethodName(String methodName);

    protected abstract int expectedNbParameter();

}
