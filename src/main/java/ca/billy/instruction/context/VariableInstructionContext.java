package ca.billy.instruction.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLineContainer.LineContext;
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
    public void add(BillyInstruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public List<VariableDefinitionInstruction> getFrameVariables() {
        List<VariableDefinitionInstruction> variables = new ArrayList<>();

        BillyInstructionContext context = this;
        while (context instanceof VariableInstructionContext) {
            context
                    .getInstructions()
                    .stream()
                    .filter(VariableDefinitionInstruction.class::isInstance)
                    .map(VariableDefinitionInstruction.class::cast)
                    .filter(v -> v.getIndex() != null)
                    .forEach(v -> variables.add(v));
            context = context.getParent();
        }

        variables.sort(Comparator.comparing(VariableDefinitionInstruction::getIndex));
        return variables;
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
    public VariableDefinitionInstruction findLocalVariable(String variableName) {
        return (VariableDefinitionInstruction) getInstructions()
                .stream()
                .filter(VariableDefinitionInstruction.class::isInstance)
                .filter(v -> ((VariableDefinitionInstruction) v).getName().equals(variableName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MethodDefinition findDefaultMethod(String methodName) {
        // By default a BillyInstructionContext don't have default method so it's lets his parent try
        return getParent().findDefaultMethod(methodName);
    }

    @Override
    public MethodDefinition findMethod(String methodName) {
        // By default a BillyInstructionContext don't have method so it's lets his parent try
        return getParent().findMethod(methodName);
    }

    @Override
    public MethodDefinition findLocalMethod(String methodName) {
        return null;
    }

    @Override
    public boolean isExistingVariable(String variableName) {
        return findVariable(variableName) != null;
    }

    @Override
    public boolean isExistingLocalVariable(String variableName) {
        return findLocalVariable(variableName) != null;
    }

    @Override
    public boolean isExistingMethod(String methodName) {
        return findMethod(methodName) != null;
    }

    @Override
    public boolean isExistingLocalMethod(String methodName) {
        return findLocalMethod(methodName) != null;
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
