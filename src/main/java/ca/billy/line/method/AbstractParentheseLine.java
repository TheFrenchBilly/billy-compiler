package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.BillyLine;

// TODO MODIFY for parameters ! 
// Expression in parameter ?
public abstract class AbstractParentheseLine implements BillyLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        int index = line.indexOf(Const.START_PARENTHESES);

        if (index == -1 || !line.endsWith(Const.END_PARENTHESES))
            return false;

        return isValidMethodName(line.substring(0, index)) && isValidParameters(extractParameters(line), instructionContext);
    }

    protected String[] extractParameters(String line) {
        int index = line.indexOf(Const.START_PARENTHESES);
        String[] args = line.substring(index + 1, line.length() - 1).split(Const.COMMA);
        if (args.length == 1 && Const.EMPTY.equals(args[0])) {
            return new String[0];
        }
        for (int i = 0; i < args.length; ++i) {
            args[i] = args[i].trim();
        }
        return args;
    }

    protected String extractMethodName(String line) {
        int index = line.indexOf(Const.START_PARENTHESES);
        return line.substring(0, index);
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
