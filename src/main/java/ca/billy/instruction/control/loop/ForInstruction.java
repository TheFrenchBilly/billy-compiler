package ca.billy.instruction.control.loop;

import org.apache.bcel.Const;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.Branch;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.TmpContext;

public class ForInstruction extends AbstractForInstruction {

    private BillyCodeInstruction initInstruction;

    private Expression expression;

    private BillyCodeInstruction incrementInstruction;

    public ForInstruction(BillyInstructionContext parent, BillyCodeInstruction initInstruction, Expression expression, BillyCodeInstruction incrementInstruction) {
        super(parent);
        this.initInstruction = initInstruction;
        this.expression = expression;
        this.incrementInstruction = incrementInstruction;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        BillyCodeInstructionArgs forArgs = args.toBuilder().context(this).build();
        TmpContext tmpContext = new TmpContext(args.getContext());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();
        Branch endBranch = new Branch(InstructionFactory.createBranchInstruction(Const.IFNE, null), forArgs);
        Branch gotoBranch = new Branch(new GOTO(null), tmpArgs);
        
        if (initInstruction != null) {
            add(initInstruction);
            tmpContext.add(initInstruction);
            initInstruction.build(forArgs);
        }

        gotoBranch.buildBranch();
        endBranch.setTarget(args.getIl().append(new NOP()));
        
        for (BillyInstruction ins : getInstructions()) {
            if (initInstruction == null || initInstruction != ins) {
                ((BillyCodeInstruction) ins).build(forArgs);
            }
        }

        if (incrementInstruction != null) {
            incrementInstruction.build(forArgs);
        }
        
        gotoBranch.setTarget(args.getIl().append(new NOP()));
        
        expression.build(forArgs);
        endBranch.buildBranch();
        
        setBreakTarget(args);
    }

}
