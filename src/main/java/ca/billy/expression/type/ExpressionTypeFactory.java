package ca.billy.expression.type;

import org.apache.bcel.generic.Type;

import ca.billy.expression.instruction.builder.ArrayEqualsExpressionBuilder;
import ca.billy.expression.instruction.builder.BinaryExpressionBuilder;
import ca.billy.expression.instruction.builder.CmpExpressionBuilder;
import ca.billy.expression.instruction.builder.StringConcatExpressionBuilder;
import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionTypeFactory {

    public static SimpleExpressionType createBinaryExpressionType(EnumType type, String op) {
        SimpleExpressionType expressionType = new SimpleExpressionType();
        expressionType.left = expressionType.right = expressionType.out = type;
        expressionType.builder = new BinaryExpressionBuilder(op, type.getBcelType());
        return expressionType;
    }

    public static SimpleExpressionType createBoolBinaryExpressionType(String op) {
        SimpleExpressionType expressionType = new SimpleExpressionType();
        expressionType.left = expressionType.right = expressionType.out = EnumType.BOOLEAN;
        expressionType.builder = new BinaryExpressionBuilder(op, Type.INT);
        return expressionType;
    }

    public static SimpleExpressionType createStringConcatExpressionType(EnumType left, EnumType right) {
        SimpleExpressionType expressionType = new SimpleExpressionType();
        expressionType.left = left;
        expressionType.right = right;
        expressionType.out = EnumType.STRING;
        expressionType.builder = new StringConcatExpressionBuilder(left.getBcelType());
        return expressionType;
    }

    public static SimpleExpressionType createCmpExpressionType(EnumType type, String op) {
        SimpleExpressionType expressionType = new SimpleExpressionType();
        expressionType.left = expressionType.right = type;
        expressionType.out = EnumType.BOOLEAN;
        expressionType.builder = new CmpExpressionBuilder(op, type.getBcelType());
        return expressionType;
    }
    
    public static SimpleExpressionType createArrayEqualExpressionType(String op) {
        SimpleExpressionType expressionType = new SimpleExpressionType();
        expressionType.left = expressionType.right = EnumType.ANY_ARRAY;
        expressionType.out = EnumType.BOOLEAN;
        expressionType.builder = new ArrayEqualsExpressionBuilder(op);
        return expressionType;
    }
}
