package ca.billy.expression.instruction;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.call.ArrayLengthMethodCallInstruction;
import ca.billy.instruction.method.call.ReadLineMethodCallInstruction;
import ca.billy.type.EnumType;

public class MethodExpressionInstruction implements SimpleExpressionInstruction {

    private MethodDefinition methodDefinition;

    private Expression[] argsExp;

    public MethodExpressionInstruction(MethodDefinition methodDefinition, Expression[] argsExp) {
        this.methodDefinition = methodDefinition;
        this.argsExp = argsExp;
    }

    @Override
    public EnumType getResultType() {
        return methodDefinition.getReturnEnumType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        
        // TODO make more that readLine usable
        if (methodDefinition.getName().equals(Const.READ_LINE)) {
            new ReadLineMethodCallInstruction(true).build(args);
        } else if (methodDefinition.getName().equals(Const.ARRAY_LENGTH_LINE)) {
            new ArrayLengthMethodCallInstruction(argsExp[0], true).build(args);
        }
    }
}
