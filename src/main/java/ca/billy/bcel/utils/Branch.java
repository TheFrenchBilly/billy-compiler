package ca.billy.bcel.utils;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.Getter;

public class Branch {

    private BranchInstruction branch;
    private BillyCodeInstructionArgs args;
    private BranchHandle branchHandle;

    @Getter
    private Type[] stacks;

    @Getter
    private Type[] locals;

    public Branch(BranchInstruction branch, BillyCodeInstructionArgs args, Type... stacks) {
        this.branch = branch;
        this.args = args;
        this.stacks = stacks;
        args.getStackMapBuilder().addFrame(this);
    }

    public void setTarget(InstructionHandle target) {
        branch.setTarget(target);
        locals = args.getContext().getFrameVariables().stream().map(t -> t.getBcelType()).toArray(Type[]::new);
    }

    public void buildBranch() {
        branchHandle = args.getIl().append(branch);
    }
    
    public int getTargetPosition() {
        return branchHandle.getTarget().getPosition();
    }

}
