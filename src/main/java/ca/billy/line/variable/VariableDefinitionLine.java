package ca.billy.line.variable;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;
import ca.billy.util.VariableUtil;

public class VariableDefinitionLine implements BillyLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (line.getLine().indexOf(Const.SPACE) == -1)
            return false;

        String[] s = getVariableInfo(line.getLine());
        return EnumType.anyMatch(s[0]) && VariableUtil.isValidName(s[1]);
    }

    // { Enumtype as String, The rest of the Line - Enumtype as String}
    protected String[] getVariableInfo(String line) {
        int index = line.indexOf(Const.SPACE);
        return new String[] { line.substring(0, index), line.substring(index + 1).trim() };
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String[] s = getVariableInfo(line.getLine());
        if (instructionContext.isExistingLocalVariable(s[1]))
            throw new BillyException("variable already define : " + s[1]);

        return new VariableDefinitionInstruction(s[1], EnumType.getEnumType(s[0]), line.getLineNumber());
    }

}
