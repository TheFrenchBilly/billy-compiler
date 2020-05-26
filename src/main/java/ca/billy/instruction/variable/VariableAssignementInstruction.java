package ca.billy.instruction.variable;

import ca.billy.BillyException;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;

// Work for attribut
public class VariableAssignementInstruction implements BillyCodeInstruction {

    private String name;

    private Expression expression;

    private int lineNumber;

    public VariableAssignementInstruction(String name, Expression expression) {
        super();
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        VariableDefinitionInstruction variableDefinitionInstruction = args.getContext().findVariable(name);
        if (variableDefinitionInstruction == null)
            throw new BillyException("variable not define : " + name, lineNumber);

        expression.setExpectedReturn(variableDefinitionInstruction.getEnumType());
        expression.build(args);
        variableDefinitionInstruction.buildStore(args);
    }

}
