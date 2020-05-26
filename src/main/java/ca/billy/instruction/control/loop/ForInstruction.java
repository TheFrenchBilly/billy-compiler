package ca.billy.instruction.control.loop;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.Const;

import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;

public class ForInstruction extends AbstractLoopInstruction {

    private BillyCodeInstruction initInstruction;

    private Expression expression;

    private BillyCodeInstruction incrementInstruction;

    public ForInstruction(BillyInstructionContext parent, BillyCodeInstruction initInstruction, Expression expression, BillyCodeInstruction incrementInstruction) {
        super(parent);
        this.initInstruction = initInstruction;
        this.expression = expression;
        this.incrementInstruction = incrementInstruction;
    }

    @Override
    protected short getJumpOpCode() {
        return Const.IFNE;
    }

    @Override
    protected List<VariableDefinitionInstruction> init(BillyCodeInstructionArgs loopArgs, TmpContext tmpContext) {
        List<VariableDefinitionInstruction> variables = new ArrayList<>();

        if (initInstruction != null) {
            add(initInstruction);
            tmpContext.add(initInstruction);
            initInstruction.build(loopArgs);
            if (initInstruction instanceof VariableDefinitionInstruction) {
                variables.add((VariableDefinitionInstruction) initInstruction);
            }
        }

        return variables;
    }

    @Override
    protected void increment(BillyCodeInstructionArgs loopArgs) {
        if (incrementInstruction != null) {
            incrementInstruction.build(loopArgs);
        }
    }

    @Override
    protected void expression(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs) {
        expression.build(loopArgs);
    }

}
