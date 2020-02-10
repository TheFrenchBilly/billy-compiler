package ca.billy.instruction;

import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import ca.billy.bcel.utils.stackmap.StackMapBuilder;
import ca.billy.instruction.context.BillyInstructionContext;
import lombok.Builder;
import lombok.Getter;

public interface BillyCodeInstruction extends BillyInstruction {

    void build(BillyCodeInstructionArgs args);

    @Builder(toBuilder=true)
    @Getter
    public static class BillyCodeInstructionArgs {
        private ClassGen cg;
        private InstructionList il;
        private ConstantPoolGen cp;
        private InstructionFactory factory;
        private MethodGen mg;
        private BillyInstructionContext context;
        private StackMapBuilder stackMapBuilder;
    }

}
