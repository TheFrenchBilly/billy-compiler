package ca.billy.instruction.variable;

import ca.billy.BillyException;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;

// Work for attribut
public class VariableAssignementInstruction implements BillyCodeInstruction {

    private String name;

    private String expressionString;

    private int lineNumber;

    public VariableAssignementInstruction(String name, String expressionString, int lineNumber) {
        super();
        this.name = name;
        this.expressionString = expressionString;
        this.lineNumber = lineNumber;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        VariableDefinitionInstruction variableDefinitionInstruction = args.getContext().findVariable(name);
        if (variableDefinitionInstruction == null)
            throw new BillyException("variable not define : " + name, lineNumber);

        new Expression(expressionString, variableDefinitionInstruction.getEnumType(), lineNumber).build(args);
        variableDefinitionInstruction.buildStore(args);
    }

}
