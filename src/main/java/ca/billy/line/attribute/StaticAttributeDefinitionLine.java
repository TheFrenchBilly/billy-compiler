package ca.billy.line.attribute;

import ca.billy.BillyException;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.line.variable.VariableDefinitionLine;
import ca.billy.type.EnumType;

public class StaticAttributeDefinitionLine extends VariableDefinitionLine {

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        String[] s = getVariableInfo(line);
        if (instructionContext.isExistingVariable(s[1]))
            throw new BillyException("static variable already define : " + s[1]);

        return new MainAttributeDefinitionInstruction(s[1], EnumType.getEnumType(s[0]));
    }
}
