package ca.billy.expression.instruction;

import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstExpression implements SimpleExpression {

    private Object value;

    private EnumType type;

    @Override
    public EnumType getResultType() {
        return type;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        args.getIl().append(type.createPush(args.getCp(), value));
    }

    @Override
    public EnumType getType(BillyInstructionContext context) {
        return type;
    }
}
