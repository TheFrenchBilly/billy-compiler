package ca.billy.line.variable;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;
import ca.billy.util.VariableUtil;

public class VariableDefinitionAssignementLine extends VariableDefinitionLine {

    @Override
    public boolean isValid(LineWrapper line, BillyInstructionContext instructionContext) {
        if (line.getLine().indexOf(Const.OPT_ASSIGNEMENT) == -1)
            return false;

        String[] s = getVariableInfo(line.getLine());
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableUtil.splitAssignement(s[1]);
        return enumType != null && VariableUtil.isValidName(s[0]);
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String[] s = getVariableInfo(line.getLine());
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableUtil.splitAssignement(s[1]);
        if (instructionContext.isExistingLocalVariable(s[0]))
            throw new BillyException("variable already define : " + s[0]);

        return new VariableDefinitionInstruction(s[0], enumType, new Expression(s[1], enumType, line.getLineNumber()));
    }

}
