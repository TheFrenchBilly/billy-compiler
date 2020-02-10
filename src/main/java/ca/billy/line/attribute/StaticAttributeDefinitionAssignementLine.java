package ca.billy.line.attribute;

import ca.billy.BillyException;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.variable.VariableAssignementLine;
import ca.billy.line.variable.VariableDefinitionAssignementLine;
import ca.billy.type.EnumType;

public class StaticAttributeDefinitionAssignementLine extends VariableDefinitionAssignementLine {

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        String[] s = getVariableInfo(line);
        EnumType enumType = EnumType.getEnumType(s[0]);
        s = VariableAssignementLine.getVariableInfo(s[1]);
        if (instructionContext.isExistingVariable(s[0]))
            throw new BillyException("static variable already define : " + s[0]);

        return new MainAttributeDefinitionInstruction(s[0], enumType, expressionProcessor.buildExpression(s[1], enumType, instructionContext));
    }

}
