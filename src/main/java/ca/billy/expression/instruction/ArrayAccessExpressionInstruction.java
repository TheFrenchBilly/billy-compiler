package ca.billy.expression.instruction;

import org.apache.bcel.generic.InstructionFactory;

import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayAccessExpressionInstruction implements SimpleExpressionInstruction {
      
    private VariableDefinitionInstruction variableDefinitionInstruction;
    
    // TODO should it be? private IExpression expression;
    // To have expression inside the []
    private SimpleExpressionInstruction expression;
    
    @Override
    public EnumType getResultType() {
        return variableDefinitionInstruction.getEnumType().getArrayType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        variableDefinitionInstruction.buildLoad(args);
        expression.build(args);
        args.getIl().append(InstructionFactory.createArrayLoad(variableDefinitionInstruction.getEnumType().getArrayBcelType()));
    }
}
