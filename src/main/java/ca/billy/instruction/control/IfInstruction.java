package ca.billy.instruction.control;

import java.util.ArrayList;
import java.util.List;

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
import ca.billy.instruction.context.CodeInstructionContext;
import ca.billy.line.BillyLineContainer.LineContext;
import lombok.Getter;

public class IfInstruction extends CodeInstructionContext implements BillyCodeInstruction {

    protected Expression expression;

    @Getter
    protected Branch gotoBranch;

    public IfInstruction(BillyInstructionContext parent, Expression expression) {
        super(parent);
        this.expression = expression;
    }

    @Override
    public LineContext getLineEndContext() {
        return LineContext.END_IF;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        BillyCodeInstructionArgs ifArgs = args.toBuilder().context(this).build();
        Branch falseBranch = new Branch(InstructionFactory.createBranchInstruction(Const.IFEQ, null), ifArgs);
        gotoBranch = new Branch(new GOTO(null), ifArgs);

        expression.build(args);
        falseBranch.buildBranch();

        for (BillyInstruction ins : getInstructions()) {
            ((BillyCodeInstruction) ins).build(ifArgs);
        }
        gotoBranch.buildBranch();

        InstructionHandle nopInstruction = args.getIl().append(new NOP());
        falseBranch.setTarget(nopInstruction);
        gotoBranch.setTarget(nopInstruction);
    }

    static List<IfInstruction> getLastIf(BillyInstructionContext context, BillyInstruction instruction) {
        List<BillyInstruction> ins = context.getInstructions();
        int pos;
        for (pos = 0; pos < ins.size(); ++pos)
            if (ins.get(pos) == instruction)
                break;

        if (pos == ins.size())
            throw new IllegalArgumentException("instruction not found");

        List<IfInstruction> ifInstruction = new ArrayList<>();
        for (int i = pos - 1; i > -1; --i)
            if (!(ins.get(i) instanceof IfInstruction))
                return ifInstruction;
            else
                ifInstruction.add((IfInstruction)ins.get(i));

        if (ifInstruction.isEmpty())
            throw new IllegalArgumentException("IfInstruction not found");
        
        return ifInstruction;
    }

}
