package ca.billy.instruction.control.loop;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionHandle;

import ca.billy.bcel.utils.Branch;
import ca.billy.instruction.BillyCodeInstruction;
import lombok.Getter;

public class BreakInstruction implements BillyCodeInstruction {

    @Getter
    private Branch branch;

    @Override
    public void build(BillyCodeInstructionArgs args) {
        branch = new Branch(new GOTO(null), args);
        branch.buildBranch();
    }

    void setTarget(InstructionHandle target, BillyCodeInstructionArgs args) {
        branch.setArgs(args);
        branch.setTarget(target);
    }
}
