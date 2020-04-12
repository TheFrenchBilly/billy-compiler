package ca.billy.instruction.control;

import org.apache.bcel.Const;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.BranchUtils;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;

public class ElseIfInstruction extends IfInstruction {

    public ElseIfInstruction(BillyInstructionContext parent, Expression expression) {
        super(parent, expression);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        BranchInstruction falseHandle = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        gotoHandle = new GOTO(null);

        expression.build(args);
        BranchUtils.createBranch(falseHandle, args);

        BillyCodeInstructionArgs contextArgs = args.toBuilder().context(this).build();
        for (BillyInstruction ins : getInstructions()) {
            ((BillyCodeInstruction) ins).build(contextArgs);
        }
        BranchUtils.createBranch(gotoHandle, args);

        InstructionHandle nopInstruction = args.getIl().append(new NOP());
        falseHandle.setTarget(nopInstruction);
        gotoHandle.setTarget(nopInstruction);
        IfInstruction.getLastIf(args.getContext(), this).forEach(IfInstruction -> IfInstruction.getGotoHandle().setTarget(nopInstruction));
    }

}
