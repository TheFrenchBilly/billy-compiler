package ca.billy.expression.instruction;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionFactory {
    
    private static String VAR_REGEX = "[a-zA-Z]+";
    
    public static SimpleExpression createSimpleExpression(String value, BillyInstructionContext instructionContext) {
        EnumType type = EnumType.findTypeWithValue(value);
        if (type != null) {
            return new ConstExpression(type.getValue(value), type);
        } else if (value.indexOf(Const.START_PARENTHESES) != -1 && value.endsWith(Const.END_PARENTHESES)) {
            return new MethodExpression(value);
        } else if (value.matches(VAR_REGEX)) {
            VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(value);
            if (variableDefinitionInstruction == null) {
                throw new BillyException("variable not define : " + value);
            }
            return new VariableExpression(value, variableDefinitionInstruction.getEnumType());
        }
        throw new BillyException("No available type for the input " + value);
    }

}
