package ca.billy.instruction.variable;

import ca.billy.expression.instruction.IExpression;
import ca.billy.instruction.AlwaysValidBillyInstruction;
import ca.billy.instruction.BillyCodeInstruction;

// Work for attribut
public class VariableAssignementInstruction implements BillyCodeInstruction, AlwaysValidBillyInstruction {

    private String name;

    private IExpression expression;

    public VariableAssignementInstruction(String name, IExpression expression) {
        super();
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expression.build(args);
        args.getContext().findVariable(name).buildStore(args);
    }

}
