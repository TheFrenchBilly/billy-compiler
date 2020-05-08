package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.instruction.context.TmpContext;

public abstract class AbstractExpressionBuilder implements ExpressionBuilder {

    @Override
    public void build(IExpressionInstruction left, IExpressionInstruction right, BillyCodeInstructionArgs args) {       
        left.build(args);
        
        TmpContext tmpContext = new TmpContext(args.getContext(), left.getResultType());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();
        right.build(tmpArgs);
        
        build(args);
    }

    protected abstract void build(BillyCodeInstructionArgs args);

}
