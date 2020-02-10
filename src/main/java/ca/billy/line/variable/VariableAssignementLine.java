package ca.billy.line.variable;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableAssignementInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLine;

public class VariableAssignementLine implements BillyLine {

    @Override
    public boolean isValid(String line, BillyInstructionContext instructionContext) {
        return line.indexOf(Const.OPT_ASSIGNEMENT) != -1;
    }

    // { variable name, the value to assigne}
    // dont support multiple =
    public static String[] getVariableInfo(String line) {
        int index = line.indexOf(Const.OPT_ASSIGNEMENT);
        return new String[] { line.substring(0, index).trim(), line.substring(index + 1).trim() };
    }

    @Override
    public BillyInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext,  ExpressionProcessor expressionProcessor) {
        String[] s = getVariableInfo(line);
        VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(s[0]);
        if (variableDefinitionInstruction == null)
            throw new BillyException("variable not define : " + s[0]);

        return new VariableAssignementInstruction(s[0], expressionProcessor.buildExpression(s[1], variableDefinitionInstruction.getEnumType(), instructionContext));
    }

}
