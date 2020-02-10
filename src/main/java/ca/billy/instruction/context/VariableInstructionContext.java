package ca.billy.instruction.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.LineContainer.LineContext;
import lombok.Getter;

public abstract class VariableInstructionContext implements BillyInstructionContext {

    @Getter
    private List<BillyInstruction> instructions;

    @Getter
    private BillyInstructionContext parent;

    public VariableInstructionContext(BillyInstructionContext parent) {
        this.parent = parent;
        instructions = new ArrayList<>();
    }

    @Override
    public void valid(BillyInstructionContext instructionContext) {
        instructions.forEach((i) -> i.valid(instructionContext));
    }
    
    @Override
    public void add(BillyInstruction instruction) {
        instructions.add(instruction);
    }
    
    @Override
    public List<BillyInstruction> getIntructions() {
        return instructions;
    }

    // By default a BillyInstructionContext don't have method so it's lets his parent try
    @Override
    public MethodDefinition findMethod(String methodName) {
        return getParent().findMethod(methodName);
    }

    @Override
    public VariableDefinitionInstruction findVariable(String variableName) {
        return (VariableDefinitionInstruction) getInstructions()
                .stream()
                .filter(VariableDefinitionInstruction.class::isInstance)
                .filter(v -> ((VariableDefinitionInstruction) v).getName().equals(variableName))
                .findFirst()
                .orElse(getParent().findVariable(variableName));
    }

    @Override
    public List<VariableDefinitionInstruction> getVariables() {
        List<VariableDefinitionInstruction> variables = getInstructions()
                .stream()
                .filter(VariableDefinitionInstruction.class::isInstance)
                .map(VariableDefinitionInstruction.class::cast)
                .filter(v -> v.getIndex() != null)
                .sorted(Comparator.comparing(VariableDefinitionInstruction::getIndex))
                .collect(Collectors.toList());

        BillyInstructionContext parents = getParent();
        while (parents instanceof VariableInstructionContext) {
            variables.addAll(parents.getVariables());
            parents = parents.getParent();
        }

        variables.sort(Comparator.comparing(VariableDefinitionInstruction::getIndex));

        return variables;
    }

    @Override
    public boolean isExistingVariable(String variableName) {
        return findVariable(variableName) != null;
    }

    @Override
    public boolean isExistingMethod(String methodName) {
        return findMethod(methodName) != null;
    }

    @Override
    public LineContext getLineEndContext() {
        return LineContext.NONE;
    }

    @Override
    public LineContext getLineContext() {
        return LineContext.CODE;
    }
}
