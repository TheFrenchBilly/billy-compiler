package ca.billy.expression.instruction.leaf;

import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ConstExpressionInstruction implements LeafExpressionInstruction {

    @Getter
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
