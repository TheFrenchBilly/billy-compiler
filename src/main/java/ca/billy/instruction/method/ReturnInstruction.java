package ca.billy.instruction.method;

import org.apache.bcel.generic.InstructionConst;

import ca.billy.instruction.BillyCodeInstruction;

public class ReturnInstruction implements BillyCodeInstruction {

    @Override
    public void build(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionConst.RETURN);     
    }
}
