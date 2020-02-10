package ca.billy.line.variable;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLine;
import ca.billy.type.EnumType;

public class VariableDefinitionLine implements BillyLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        if (line.indexOf(Const.SPACE) == -1)
            return false;

        String[] s = getVariableInfo(line);
        return EnumType.anyMatch(s[0]) && s[1].matches("[a-zA-Z]+");
    }

    // { Enumtype as String, The rest of the Line - Enumtype as String}
    protected String[] getVariableInfo(String line) {
        int index = line.indexOf(Const.SPACE);
        return new String[] { line.substring(0, index), line.substring(index + 1).trim() };
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        String[] s = getVariableInfo(line);
        if (instructionContext.isExistingVariable(s[1]))
            throw new BillyException("variable already define : " + s[1]);

        return new VariableDefinitionInstruction(s[1], EnumType.getEnumType(s[0]));
    }

}
