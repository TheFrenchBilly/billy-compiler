package ca.billy.instruction.control.loop;

import java.util.List;

import org.apache.bcel.Const;

import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;

public class WhileInstruction extends AbstractLoopInstruction {

    private Expression expression;

    public WhileInstruction(BillyInstructionContext parent, Expression expression) {
        super(parent);
        this.expression = expression;
    }

    @Override
    protected short getJumpOpCode() {
        return Const.IFNE;
    }

    @Override
    protected void increment(BillyCodeInstructionArgs loopArgs) {
    }

    @Override
    protected void expression(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs) {
       expression.build(loopArgs);
    }

}
