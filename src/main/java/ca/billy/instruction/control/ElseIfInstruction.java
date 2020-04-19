package ca.billy.instruction.control;

import org.apache.bcel.Const;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.Branch;
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
        BillyCodeInstructionArgs contextArgs = args.toBuilder().context(this).build();
        Branch falseBranch = new Branch(InstructionFactory.createBranchInstruction(Const.IFEQ, null), contextArgs);
        gotoBranch = new Branch(new GOTO(null), contextArgs);

        expression.build(args);
        falseBranch.buildBranch();
        for (BillyInstruction ins : getInstructions()) {
            ((BillyCodeInstruction) ins).build(contextArgs);
        }
        gotoBranch.buildBranch();

        InstructionHandle nopInstruction = args.getIl().append(new NOP());
        falseBranch.setTarget(nopInstruction);
        gotoBranch.setTarget(nopInstruction);
        IfInstruction.getLastIf(args.getContext(), this).forEach(IfInstruction -> IfInstruction.getGotoBranch().setTarget(nopInstruction));
    }

}
