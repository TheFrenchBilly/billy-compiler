package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.SimpleExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

public interface ExpressionBuilder {
      
    void build(SimpleExpressionInstruction left, SimpleExpressionInstruction right, BillyCodeInstructionArgs args);
    
    void build(SimpleExpressionInstruction right, BillyCodeInstructionArgs args);

}
