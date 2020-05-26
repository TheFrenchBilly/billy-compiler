package ca.billy.instruction.control;

import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;

public class ElseIfInstruction extends IfInstruction {

    public ElseIfInstruction(BillyInstructionContext parent, Expression expression) {
        super(parent, expression);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        super.build(args);
        IfInstruction.getLastIf(args.getContext(), this).forEach(IfInstruction -> IfInstruction.getGotoBranch().setTarget(args.getIl().getEnd()));
    }

}
