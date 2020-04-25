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
        int spaceIndex = line.getLine().indexOf(Const.SPACE);
        String[] exps = line.getLine().substring(spaceIndex + 1, line.getLine().length() - 1).split(";");

        for (int i = 0; i < 2; ++i) {
            exps[i] = exps[i].trim();
        }

        String indexName = null, loopName = null;
        int commaIndex = exps[0].indexOf(Const.COMMA);

        if (commaIndex == -1) {
            loopName = exps[0];
        } else {
            String[] vars = exps[0].split(Const.COMMA);
            for (int i = 0; i < 2; ++i) {
                vars[i] = vars[i].trim();
            }
            indexName = vars[0];
            loopName = vars[1];

            if (!VariableUtil.isValidName(indexName)) {
                throw new BillyException("Invalid index name " + exps[0]);
            }
        }

        if (!VariableUtil.isValidName(loopName)) {
            throw new BillyException("Invalid variable name " + exps[0]);
        }

        return new ForEachInstruction(instructionContext, indexName, loopName, new Expression(exps[1], EnumType.ANY_ARRAY, line.getLineNumber()));
    }

}
