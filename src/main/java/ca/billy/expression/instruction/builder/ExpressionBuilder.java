package ca.billy.expression.instruction.builder;

import ca.billy.expression.instruction.SimpleExpression;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;

public interface ExpressionBuilder {
      
    void build(SimpleExpression left, SimpleExpression right, BillyCodeInstructionArgs args);
    
    void build(SimpleExpression right, BillyCodeInstructionArgs args);

}
