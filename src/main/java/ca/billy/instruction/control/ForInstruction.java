package ca.billy.instruction.control;

import org.apache.bcel.Const;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.BranchUtils;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;

public class ForInstruction extends VariableInstructionContext implements BillyCodeInstruction {

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
        BranchInstruction endHandle = InstructionFactory.createBranchInstruction(Const.IFNE, null);
        GOTO gotoHandle = new GOTO(null);
        
        if (initInstruction != null) {
            getInstructions().add(initInstruction);
            initInstruction.build(forArgs);
        }

        BranchUtils.createBranch(gotoHandle, forArgs);
        endHandle.setTarget(args.getIl().append(new NOP()));
        
        for (BillyInstruction ins : getInstructions()) {
            if (initInstruction == null || initInstruction != ins) {
                ((BillyCodeInstruction) ins).build(forArgs);
            }
        }

        if (incrementInstruction != null) {
            incrementInstruction.build(forArgs);
        }
        
        gotoHandle.setTarget(args.getIl().append(new NOP()));
        
        expression.build(forArgs);
        BranchUtils.createBranch(endHandle, forArgs);  
    }

}
