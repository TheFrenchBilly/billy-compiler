package ca.billy.line.attribute;

import ca.billy.BillyException;
import ca.billy.expression.ExpressionFactory;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.LineWrapper;
import ca.billy.line.variable.VariableDefinitionAssignementLine;
import ca.billy.type.EnumType;
import ca.billy.util.VariableUtil;

public class StaticAttributeDefinitionAssignementLine extends VariableDefinitionAssignementLine {

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String[] s = getVariableInfo(line.getLine());
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableUtil.splitAssignement(s[1]);
        if (instructionContext.isExistingLocalVariable(s[0]))
            throw new BillyException("static variable already define : " + s[0]);

        return new MainAttributeDefinitionInstruction(s[0], enumType, ExpressionFactory.createExpression(s[1], enumType, line.getLineNumber()));
    }

}
