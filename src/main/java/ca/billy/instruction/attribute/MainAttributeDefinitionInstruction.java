package ca.billy.instruction.attribute;

import ca.billy.expression.Expression;
import ca.billy.type.EnumType;

public class MainAttributeDefinitionInstruction extends AttributeDefinitionInstruction {

    public MainAttributeDefinitionInstruction(String name, EnumType enumType, int lineNumber) {
        super(name, enumType, new Expression(enumType, lineNumber), true);
    }

    public MainAttributeDefinitionInstruction(String name, EnumType enumType, Expression expression) {
        super(name, enumType, expression, true);
    }

    @Override
    public void buildStore(BillyCodeInstructionArgs args) {
        // "Main" is the only class for now
        args.getIl().append(args.getFactory().createPutStatic("Main", name, getEnumType().getBcelType()));
    }

    @Override
    public void buildLoad(BillyCodeInstructionArgs args) {
        // "Main" is the only class for now
        args.getIl().append(args.getFactory().createGetStatic("Main", name, getEnumType().getBcelType()));
    }
}
