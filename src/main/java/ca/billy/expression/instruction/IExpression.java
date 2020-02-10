package ca.billy.expression.instruction;

import ca.billy.instruction.AlwaysValidBillyInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.type.EnumType;

public interface IExpression extends BillyCodeInstruction, AlwaysValidBillyInstruction {

    /**
     * Before the call of the method {@link BillyCodeInstruction#build} the result cannot be 100% trusted.<br>
     *
     * @return The result {@link EnumType}
     * @See {@link IExpression#matchResultType}
     */
    EnumType getResultType();

    /**
     * Check if the result type match the type.
     * 
     * @param type the {@link EnumType}
     * @return if the type is matching the result type; otherwise false
     * @See {@link IExpression#matchResultType}
     */
    default boolean matchResultType(EnumType type) {
        return getResultType().typeMatch(type);
    }

}
