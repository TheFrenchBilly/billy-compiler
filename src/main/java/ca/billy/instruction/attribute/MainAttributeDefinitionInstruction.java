package ca.billy.instruction.attribute;

import ca.billy.expression.instruction.IExpression;
import ca.billy.expression.instruction.ConstExpression;
import ca.billy.type.EnumType;

public class MainAttributeDefinitionInstruction extends AttributeDefinitionInstruction {

    public MainAttributeDefinitionInstruction(String name, EnumType enumType) {
        super(name, enumType, new ConstExpression(enumType.getTypeInfo().getDefaultValue(), enumType), true);
    }

    public MainAttributeDefinitionInstruction(String name, EnumType enumType, IExpression expression) {
        super(name, enumType, expression, true);
    }

    @Override
    public void buildStore(BillyCodeInstructionArgs args) {
        // "Main" is the only class for now
        args.getIl().append(args.getFactory().createPutStatic("Main", name, getEnumType().getTypeInfo().getBcelType()));
    }

    @Override
    public void buildLoad(BillyCodeInstructionArgs args) {
        // "Main" is the only class for now
        args.getIl().append(args.getFactory().createGetStatic("Main", name, getEnumType().getTypeInfo().getBcelType()));
    }
}
