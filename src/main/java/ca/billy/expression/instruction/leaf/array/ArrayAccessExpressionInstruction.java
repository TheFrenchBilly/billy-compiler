package ca.billy.expression.instruction.leaf.array;

import org.apache.bcel.generic.InstructionFactory;

import ca.billy.expression.instruction.leaf.LeafExpressionInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayAccessExpressionInstruction implements LeafExpressionInstruction {
      
    private VariableDefinitionInstruction arrayVariableDefinitionInstruction;
    
    // TODO should it be? private IExpression expression;
    // To have expression inside the []
    private LeafExpressionInstruction expression;
    
    @Override
    public EnumType getResultType() {
        return arrayVariableDefinitionInstruction.getEnumType().getArrayType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        arrayVariableDefinitionInstruction.buildLoad(args);
        expression.build(args);
        args.getIl().append(InstructionFactory.createArrayLoad(arrayVariableDefinitionInstruction.getEnumType().getArrayBcelType()));
    }
}
