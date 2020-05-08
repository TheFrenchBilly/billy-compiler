package ca.billy.instruction.variable;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LocalVariableGen;

import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.instruction.leaf.ConstExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.Getter;

public class VariableDefinitionInstruction implements BillyCodeInstruction {

    @Getter
    protected String name;

    @Getter
    protected EnumType enumType;

    protected Expression expression;

    private LocalVariableGen lg;

    public VariableDefinitionInstruction(String name, EnumType enumType, int lineNumber) {
        super();
        this.name = name;
        this.enumType = enumType;
        expression = ExpressionFactory.createExpressionWithInstruction(new ConstExpressionInstruction(enumType.getDefaultValue(), enumType));
    }

    public VariableDefinitionInstruction(String name, EnumType enumType, Expression expression) {
        super();
        this.name = name;
        this.enumType = enumType;
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expression.build(args);
        lg = args.getMg().addLocalVariable(name, enumType.getBcelType(), findIndex(args.getContext()), null, null);
        lg.setStart(args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), lg.getIndex())));
    }

    private int findIndex(BillyInstructionContext billyInstructionContext) {
        return billyInstructionContext.getFrameVariables().size();
    }

    public void buildStore(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), lg.getIndex()));
    }

    public void buildLoad(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createLoad(enumType.getBcelType(), lg.getIndex()));
    }

    public Integer getIndex() {
        if (lg == null) {
            return null;
        }
        return lg.getIndex();
    }

    // TODO add end to every variable ? does it really change something ?
    // never use for now
    public void setEnd(InstructionHandle end) {
        lg.setEnd(end);
    }

}
