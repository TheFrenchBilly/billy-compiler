package ca.billy.instruction.control;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NOP;

import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.CodeInstructionContext;

public class ElseInstruction extends CodeInstructionContext implements BillyCodeInstruction {

    public ElseInstruction(BillyInstructionContext parent) {
        super(parent);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {

        BillyCodeInstructionArgs contextArgs = args.toBuilder().context(this).build();
        for (BillyInstruction ins : getInstructions()) {
            ((BillyCodeInstruction) ins).build(contextArgs);
        }
        
        InstructionHandle nopInstruction = args.getIl().append(new NOP());
        IfInstruction.getLastIf(args.getContext(), this).forEach(IfInstruction -> IfInstruction.getGotoBranch().setTarget(nopInstruction));
    }

}
