package ca.billy.instruction.variable;

import org.apache.bcel.generic.InstructionFactory;

import ca.billy.BillyException;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.type.EnumType;

public class VariableArrayAssignementInstruction implements BillyCodeInstruction {

    private String name;

    private String accessExpression;

    private String expressionString;

    private int lineNumber;

    public VariableArrayAssignementInstruction(String name, String accessExpression, String expressionString, int lineNumber) {
        super();
        this.name = name;
        this.accessExpression = accessExpression;
        this.expressionString = expressionString;
        this.lineNumber = lineNumber;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        VariableDefinitionInstruction variableDefinitionInstruction = args.getContext().findVariable(name);
        if (variableDefinitionInstruction == null)
            throw new BillyException("variable not define : " + name, lineNumber);
        if (!variableDefinitionInstruction.getEnumType().isArray()) 
            throw new BillyException("variable is not a array : " + name, lineNumber);
        
        variableDefinitionInstruction.buildLoad(args);
        new Expression(accessExpression, EnumType.INTEGER, lineNumber).build(args);       
        new Expression(expressionString, variableDefinitionInstruction.getEnumType().getArrayType(), lineNumber).build(args);
        args.getIl().append(InstructionFactory.createArrayStore(variableDefinitionInstruction.getEnumType().getArrayBcelType()));
    }
}
