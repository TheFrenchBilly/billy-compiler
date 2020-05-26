package ca.billy.expression.instruction;

import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.type.EnumType;

public interface IExpressionInstruction extends BillyCodeInstruction {

    /**
     * Before the call of the method {@link BillyCodeInstruction#build} the result cannot be 100% trusted.<br>
     *
     * @return The result {@link EnumType}
     * @See {@link IExpressionInstruction#matchResultType}
     */
    EnumType getResultType();
}
