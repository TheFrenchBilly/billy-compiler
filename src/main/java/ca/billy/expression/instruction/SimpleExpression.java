package ca.billy.expression.instruction;

import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;

public interface SimpleExpression extends IExpression {

    EnumType getType(BillyInstructionContext context);
}
