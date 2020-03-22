package ca.billy.instruction.variable;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.LocalVariableGen;

import ca.billy.expression.instruction.IExpression;
import ca.billy.expression.instruction.ConstExpression;
import ca.billy.instruction.AlwaysValidBillyInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.type.EnumType;
import lombok.Getter;

public class VariableDefinitionInstruction implements BillyCodeInstruction, AlwaysValidBillyInstruction {

    @Getter
    protected String name;

    @Getter
    protected EnumType enumType;

    protected IExpression expression;

    @Getter
    private Integer index;

    public VariableDefinitionInstruction(String name, EnumType enumType) {
        super();
        this.name = name;
        this.enumType = enumType;
        expression = new ConstExpression(enumType.getDefaultValue(), enumType);
    }

    public VariableDefinitionInstruction(String name, EnumType enumType, IExpression expression) {
        super();
        this.name = name;
        this.enumType = enumType;
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        LocalVariableGen lg = args.getMg().addLocalVariable(name, enumType.getBcelType(), null, null);
        expression.build(args);
        index = lg.getIndex();
        lg.setStart(args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), index)));
    }

    public void buildStore(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), index));
    }
    
    public void buildLoad(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createLoad(enumType.getBcelType(), index));
    }

}
