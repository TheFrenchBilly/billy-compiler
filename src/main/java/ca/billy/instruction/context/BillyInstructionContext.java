package ca.billy.instruction.context;

import java.util.List;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLineContainer.LineContext;
import ca.billy.type.EnumType;

// TODO local method, but that means that variable can have the same name
// I don't think that the actual generate bytecode support that
public interface BillyInstructionContext extends BillyInstruction {
    
    public BillyInstructionContext getParent();
    
    List<BillyInstruction> getInstructions();
    
    void add(BillyInstruction instruction);
    
    MethodDefinition findMethod(String methodName);
    
    MethodDefinition findDefaultMethod(String methodName);
    
    MethodDefinition findLocalMethod(String methodName);
    
    VariableDefinitionInstruction findVariable(String variableName);
    
    VariableDefinitionInstruction findLocalVariable(String variableName);
    
    List<EnumType> getFrameVariables();
    
    boolean isExistingVariable(String variableName);
    
    boolean isExistingLocalVariable(String variableName);
    
    boolean isExistingMethod(String methodName);
    
    boolean isExistingLocalMethod(String methodName);
    
    LineContext getLineEndContext();
    
    LineContext getLineContext();
    
}
