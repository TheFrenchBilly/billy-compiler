package ca.billy.expression.instruction;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.PUSH;

import ca.billy.BillyException;
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
        args.getIl().append(push(args.getCp()));
    }

    @Override
    public EnumType getType(BillyInstructionContext context) {
        return type;
    }
    
    private PUSH push(ConstantPoolGen cp) {
        if (type == EnumType.STRING) {
            return new PUSH(cp, (String) value);
        } else if (type == EnumType.INTEGER) {
            return new PUSH(cp, (int) value);
        } else if (type == EnumType.BOOLEAN) {
            return new PUSH(cp, (boolean) value);
        }
        throw new BillyException("SHOULD NOT HAPPEN");
    }

}
