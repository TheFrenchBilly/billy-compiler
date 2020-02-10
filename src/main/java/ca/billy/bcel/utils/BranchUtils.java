package ca.billy.bcel.utils;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchUtils {

    public static void createBranch(BranchInstruction branch, BillyCodeInstructionArgs args, Type... stackType) {
        Type[] locals = args.getContext().getVariables().stream().map(v -> v.getEnumType().getTypeInfo().getBcelType()).toArray(Type[]::new);
        args.getStackMapBuilder().addFrame(args.getIl().append(branch), locals, stackType);
    }

}
