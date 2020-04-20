package ca.billy.expression.instruction;

import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class VariableExpressionInstruction implements SimpleExpressionInstruction {
    
    @Getter
    private VariableDefinitionInstruction variableDefinitionInstruction;

    @Override
    public EnumType getResultType() {
        return variableDefinitionInstruction.getEnumType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        variableDefinitionInstruction.buildLoad(args);
    }
}
