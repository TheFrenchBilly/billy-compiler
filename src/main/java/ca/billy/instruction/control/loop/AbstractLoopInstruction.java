package ca.billy.instruction.control.loop;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.Branch;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.CodeInstructionContext;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLineContainer.LineContext;

public abstract class AbstractLoopInstruction extends CodeInstructionContext implements BillyCodeInstruction {

    public AbstractLoopInstruction(BillyInstructionContext parent) {
        super(parent);
    }

    @Override
    public LineContext getLineContext() {
        return LineContext.LOOP_CODE;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        BillyCodeInstructionArgs loopArgs = args.toBuilder().context(this).build();
        TmpContext tmpContext = new TmpContext(args.getContext());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();
        Branch endBranch = new Branch(InstructionFactory.createBranchInstruction(getJumpOpCode(), null), loopArgs);
        Branch gotoBranch = new Branch(new GOTO(null), tmpArgs);

        List<VariableDefinitionInstruction> initVariables = init(loopArgs, tmpContext);
        gotoBranch.buildBranch();

        endBranch.setTarget(args.getIl().append(InstructionConst.NOP));
        VariableDefinitionInstruction loopVariable = beforeUserInstruction(initVariables, loopArgs);

        for (BillyInstruction ins : getInstructions()) {
            if (initVariables.stream().noneMatch(v -> v == ins) && loopVariable != ins) {
                ((BillyCodeInstruction) ins).build(loopArgs);
            }
        }

        increment(loopArgs);

        gotoBranch.setTarget(args.getIl().append(new NOP()));
        expression(initVariables, loopArgs);
        endBranch.buildBranch();

        setBreakTarget(tmpArgs);
    }

    protected void setBreakTarget(BillyCodeInstructionArgs args) {
        InstructionHandle target = args.getIl().append(new NOP());
        getBreakInstructions(this).forEach(b -> b.setTarget(target, args));
    }

    private List<BreakInstruction> getBreakInstructions(VariableInstructionContext context) {
        List<BreakInstruction> breaks = new ArrayList<>();
        for (BillyInstruction ins : context.getInstructions()) {
            if (ins instanceof BreakInstruction) {
                breaks.add((BreakInstruction) ins);
            } else if (ins instanceof VariableInstructionContext && !(ins instanceof AbstractLoopInstruction)) {
                breaks.addAll(getBreakInstructions((VariableInstructionContext) ins));
            }
        }
        return breaks;
    }

    protected abstract short getJumpOpCode();

    protected List<VariableDefinitionInstruction> init(BillyCodeInstructionArgs loopArgs, TmpContext tmpContext) {
        return new ArrayList<>();
    }

    protected VariableDefinitionInstruction beforeUserInstruction(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs) {
        return null;
    }

    protected abstract void increment(BillyCodeInstructionArgs loopArgs);

    protected abstract void expression(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs);

}
