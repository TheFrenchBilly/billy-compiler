package ca.billy.instruction.variable;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.LocalVariableGen;

import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodInstruction;
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
        expression = new Expression(enumType, lineNumber);
    }

    public VariableDefinitionInstruction(String name, EnumType enumType, Expression expression) {
        super();
        this.name = name;
        this.enumType = enumType;
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        lg = args.getMg().addLocalVariable(name, enumType.getBcelType(), findIndex(args.getContext()), null, null);
        expression.build(args);
        lg.setStart(args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), lg.getIndex())));
    }

    private int findIndex(BillyInstructionContext billyInstructionContext) {
        int index = 0;
        for (BillyInstruction ins : billyInstructionContext.getIntructions()) {
            if (ins == this) {
                break;
            }
            if (ins instanceof VariableDefinitionInstruction) {
                ++index;
            }
        }

        while (!(billyInstructionContext instanceof MethodInstruction)) {
            billyInstructionContext = billyInstructionContext.getParent();
        }
        
        for (BillyInstruction ins : billyInstructionContext.getIntructions()) {
            if (ins == this) {
                break;
            }
            if (ins instanceof VariableDefinitionInstruction) {
                ++index;
            }
        }
        return index;
    }

    public void buildStore(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createStore(enumType.getBcelType(), lg.getIndex()));
    }

    public void buildLoad(BillyCodeInstructionArgs args) {
        args.getIl().append(InstructionFactory.createLoad(enumType.getBcelType(), lg.getIndex()));
    }

    public Integer getIndex() {
        if (lg == null) {
            System.out.println("null");
        }
        return lg.getIndex();
    }

    // TODO add end to every variable ? does it really change something ? 
    // never use for now
    public void setEnd(InstructionHandle end) {
        lg.setEnd(end);
    }

}
