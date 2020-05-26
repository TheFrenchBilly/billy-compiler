package ca.billy.instruction.context;

import java.util.List;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLineContainer.LineContext;
import ca.billy.type.EnumType;

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
    
    List<EnumType> getStackTypes();
    
    boolean isExistingVariable(String variableName);
    
    boolean isExistingLocalVariable(String variableName);
    
    boolean isExistingMethod(String methodName);
    
    boolean isExistingLocalMethod(String methodName);
    
    LineContext getLineEndContext();
    
    LineContext getLineContext();
    
}
