package ca.billy.instruction.control;

import java.util.ArrayList;
import java.util.List;

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
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.line.BillyLineContainer.LineContext;
import lombok.Getter;

public class IfInstruction extends VariableInstructionContext implements BillyCodeInstruction {

    protected Expression expression;

    @Getter
    protected GOTO gotoHandle;

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
        BranchInstruction falseHandle = InstructionFactory.createBranchInstruction(Const.IFEQ, null);
        gotoHandle = new GOTO(null);

        expression.build(args);
        BranchUtils.createBranch(falseHandle, args);

        BillyCodeInstructionArgs ifArgs = args.toBuilder().context(this).build();
        for (BillyInstruction ins : getInstructions()) {
            ((BillyCodeInstruction) ins).build(ifArgs);
        }
        BranchUtils.createBranch(gotoHandle, args);

        InstructionHandle nopInstruction = args.getIl().append(new NOP());
        falseHandle.setTarget(nopInstruction);
        gotoHandle.setTarget(nopInstruction);
    }

    static List<IfInstruction> getLastIf(BillyInstructionContext context, BillyInstruction instruction) {
        List<BillyInstruction> ins = context.getIntructions();
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
