package ca.billy.expression.instruction;

import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstExpressionInstruction implements SimpleExpressionInstruction {

    private Object value;

    private EnumType type;

    @Override
    public EnumType getResultType() {
        return type;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        type.buildConst(args, value);
    }
}
