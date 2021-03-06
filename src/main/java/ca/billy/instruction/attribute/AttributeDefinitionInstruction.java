package ca.billy.instruction.attribute;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.FieldGen;

import ca.billy.expression.Expression;
import ca.billy.instruction.modifiers.AccessModifiers;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.Getter;

public abstract class AttributeDefinitionInstruction extends VariableDefinitionInstruction {

    @Getter
    private AccessModifiers accessModifiers;

    private boolean isStatic;

    public AttributeDefinitionInstruction(String name, EnumType enumType, Expression expression, boolean isStatic) {
        super(name, enumType, expression);
        accessModifiers = AccessModifiers.PUBLIC;
        this.isStatic = isStatic;
    }

    public void build(BillyCodeInstructionArgs args) {
        FieldGen fieldGen = new FieldGen(getAccesModifiers(), enumType.getBcelType(), name, args.getCp());
        Field f = fieldGen.getField();
        if (isStatic) {
            expression.build(args);
            args.getIl().append(args.getFactory().createPutStatic("Main", name, enumType.getBcelType()));
        }
        args.getCg().addField(f);
    }

    private int getAccesModifiers() {
        if (isStatic) {
            return accessModifiers.getFlag() | Const.ACC_STATIC;
        }

        return accessModifiers.getFlag();
    }

}
