package ca.billy.line.variable;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class VariableDefinitionAssignementLine extends VariableDefinitionLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        if (line.indexOf(Const.OPT_ASSIGNEMENT) == -1)
            return false;

        String[] s = getVariableInfo(line);
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableAssignementLine.getVariableInfo(s[1]);
        return enumType != null && s[0].matches("[a-zA-Z]+");
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        String[] s = getVariableInfo(line);
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableAssignementLine.getVariableInfo(s[1]);
        if (instructionContext.isExistingVariable(s[0]))
            throw new BillyException("variable already define : " + s[0]);

        return new VariableDefinitionInstruction(s[0], enumType, expressionProcessor.buildExpression(s[1], enumType, instructionContext));
    }

}
