package ca.billy.line.control.loop;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.control.loop.ForEachInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;
import ca.billy.util.VariableUtil;

public class ForEachLine extends AbstractForLine {

    @Override
    protected int getLength() {
        return 2;
    }

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        String[] exps = line.getLine().substring(index + 1, line.getLine().length() - 1).split(";");

        for (int i = 0; i < 2; ++i) {
            exps[i] = exps[i].trim();
        }

        if (!VariableUtil.isValidName(exps[0])) {
            throw new BillyException("Invalid variable name " + exps[0]);
        }

        return new ForEachInstruction(instructionContext, exps[0], new Expression(exps[1], EnumType.ANY_ARRAY, line.getLineNumber()));
    }

}
