package ca.billy.expression.instruction;

import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableExpression implements SimpleExpression {

    private String variableName;

    private EnumType type;

    @Override
    public EnumType getResultType() {
        return type;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        args.getContext().findVariable(variableName).buildLoad(args);
    }
    
    @Override
    public EnumType getType(BillyInstructionContext context) {
        return type;
    }

}
