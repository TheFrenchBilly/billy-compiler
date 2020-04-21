package ca.billy.instruction.control.loop;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NOP;

import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.CodeInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.line.BillyLineContainer.LineContext;

public abstract class AbstractForInstruction extends CodeInstructionContext implements BillyCodeInstruction {

    public AbstractForInstruction(BillyInstructionContext parent) {
        super(parent);
    }

    // TODO maybe
    // @Override
    // public void build(BillyCodeInstructionArgs args) {
    // BillyCodeInstructionArgs forArgs = args.toBuilder().context(this).build();
    //
    //
    // }

    public void setBreakTarget(BillyCodeInstructionArgs args) {
        InstructionHandle target = args.getIl().append(new NOP());
        getBreakInstructions(this).forEach(b -> b.setTarget(target, args));
    }

    private List<BreakInstruction> getBreakInstructions(VariableInstructionContext context) {
        List<BreakInstruction> breaks = new ArrayList<>();
        for (BillyInstruction ins : context.getInstructions()) {
            if (ins instanceof BreakInstruction) {
                breaks.add((BreakInstruction) ins);
            } else if (ins instanceof VariableInstructionContext && !(ins instanceof AbstractForInstruction)) {
                breaks.addAll(getBreakInstructions((VariableInstructionContext) ins));
            }
        }
        return breaks;
    }
    
    @Override
    public LineContext getLineContext() {
        return LineContext.LOOP_CODE;
    }
}
