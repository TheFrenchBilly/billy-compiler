package ca.billy.bcel.utils;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.type.EnumType;
import lombok.Getter;
import lombok.Setter;

public class Branch {

    private BranchInstruction branch;
    private BranchHandle branchHandle;

    @Setter
    private BillyCodeInstructionArgs args;

    @Getter
    private Type[] stacks;

    @Getter
    private Type[] locals;

    public Branch(BranchInstruction branch, BillyCodeInstructionArgs args, EnumType... stacks) {
        this.branch = branch;
        this.args = args;
        this.stacks = TypeUtil.merge(args.getContext().getStackTypes(), stacks);
        args.getStackMapBuilder().addFrame(this);
    }

    public void setTarget(InstructionHandle target) {
        branch.setTarget(target);
        locals = TypeUtil.convertType(args.getContext().getFrameVariables());
    }

    public void buildBranch() {
        branchHandle = args.getIl().append(branch);
    }

    public int getTargetPosition() {
        return branchHandle.getTarget().getPosition();
    }

}
