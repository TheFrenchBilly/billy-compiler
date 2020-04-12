package ca.billy.expression;

import static ca.billy.expression.type.ExpressionTypeFactory.*;

import java.util.Arrays;
import java.util.List;

import ca.billy.BillyException;
import ca.billy.expression.type.ExpressionType;
import ca.billy.expression.type.SimpleExpressionType;
import ca.billy.type.EnumType;
import lombok.Getter;

// No implicit cast
// No explicit cast too
@Getter
public enum OperatorEnum {
    // @formatter:off
    ADDITION("+", createAddition()),
    SUBSTRACTION("-", createNumerics("-")),
    MULTIPLICATION("*", createNumerics("*")),
    DIVISION("/", createNumerics("/")),
    MODULO("%", createNumerics("%")),
    AND("&&", createBool("&&")),
    OR("||", createBool("||")),
    OR_EXCLUSIVE("^", createBool("^")),
    EQUALS("==", createCmp("==")),
    NOT_EQUALS("!=", createCmp("!=")),
    GREATER(">", createSmallerOrGreater(">")), 
    SMALLER("<", createSmallerOrGreater("<"));
    // @formatter:on

    private String operator;

    private List<SimpleExpressionType> supportedExpressionType;

    OperatorEnum(String operator, SimpleExpressionType... supportedExpressionType) {
        this.operator = operator;
        this.supportedExpressionType = Arrays.asList(supportedExpressionType);
    }

    public ExpressionType retrieveExpressionType(EnumType left, EnumType right) {
        return supportedExpressionType.stream().filter(e -> e.getLeft().equals(left) && e.getRight().equals(right)).findFirst().orElseThrow(
                () -> new BillyException("No expression for " + left.getName() + " " + operator + " " + right.getName()));
    }

    // static method

    private static SimpleExpressionType[] createNumerics(String op) {
        return new SimpleExpressionType[] { createBinaryExpressionType(EnumType.INTEGER, op), createBinaryExpressionType(EnumType.FLOAT, op) };
    }

    private static SimpleExpressionType createBool(String op) {
        return createBoolBinaryExpressionType(op);
    }

    private static SimpleExpressionType[] createAddition() {
        return new SimpleExpressionType[] { createBinaryExpressionType(EnumType.INTEGER, "+"), createBinaryExpressionType(EnumType.FLOAT, "+"),
                createStringConcatExpressionType(EnumType.STRING, EnumType.STRING), createStringConcatExpressionType(EnumType.INTEGER, EnumType.STRING),
                createStringConcatExpressionType(EnumType.STRING, EnumType.INTEGER), createStringConcatExpressionType(EnumType.FLOAT, EnumType.STRING),
                createStringConcatExpressionType(EnumType.STRING, EnumType.FLOAT), createStringConcatExpressionType(EnumType.BOOLEAN, EnumType.STRING),
                createStringConcatExpressionType(EnumType.STRING, EnumType.BOOLEAN) };
    }

    private static SimpleExpressionType[] createCmp(String op) {
        return new SimpleExpressionType[] { createCmpExpressionType(EnumType.INTEGER, op), createCmpExpressionType(EnumType.FLOAT, op),
                createCmpExpressionType(EnumType.STRING, op), createCmpExpressionType(EnumType.BOOLEAN, op) };
    }

    private static SimpleExpressionType[] createSmallerOrGreater(String op) {
        return new SimpleExpressionType[] { createCmpExpressionType(EnumType.INTEGER, op), createCmpExpressionType(EnumType.FLOAT, op),
                createCmpExpressionType(EnumType.STRING, op) };
    }

}
