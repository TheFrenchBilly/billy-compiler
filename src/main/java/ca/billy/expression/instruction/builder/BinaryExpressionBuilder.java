package ca.billy.expression.instruction.builder;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BinaryExpressionBuilder extends AbstractExpressionBuilder {
    
    private String op;
    
    private Type type;

    @Override
    protected void build(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createBinaryOperation(op, type));      
    }
}
