package ca.billy.expression;

import static ca.billy.expression.type.ExpressionTypeFactory.createBinaryExpressionType;
import static ca.billy.expression.type.ExpressionTypeFactory.createBoolBinaryExpressionType;
import static ca.billy.expression.type.ExpressionTypeFactory.createCmpExpressionType;
import static ca.billy.expression.type.ExpressionTypeFactory.createStringConcatExpressionType;
import static ca.billy.expression.type.ExpressionTypeFactory.createArrayEqualExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.billy.BillyException;
import ca.billy.expression.type.ExpressionType;
import ca.billy.expression.type.SimpleExpressionType;
import ca.billy.type.EnumType;
import lombok.Getter;

// No implicit cast
// No explicit cast too
public enum OperatorEnum {
    // @formatter:off
    ADDITION("+"),
    SUBSTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    MODULO("%"),
    AND("&&"),
    OR("||"),
    OR_EXCLUSIVE("^"),
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER(">"), 
    SMALLER("<");
    // @formatter:on

    @Getter
    private String operator;

    private static Map<OperatorEnum, List<SimpleExpressionType>> supportedExpressionTypeMap;

    OperatorEnum(String operator) {
        this.operator = operator;
    }

    public ExpressionType retrieveExpressionType(EnumType left, EnumType right) {
        return supportedExpressionTypeMap.get(this).stream().filter(e -> e.getLeft().typeMatch(left) && e.getRight().typeMatch(right)).findFirst().orElseThrow(
                () -> new BillyException("No expression for " + left.getName() + " " + operator + " " + right.getName()));
    }

    // static method

    /** Get the values by priority (ascending) */
    public static OperatorEnum[][] getValuesByPriority() {
        // @formatter:off
        return new OperatorEnum[][] {
            new OperatorEnum[] {OR},
            new OperatorEnum[] {AND},
            new OperatorEnum[] {OR_EXCLUSIVE},
            new OperatorEnum[] {EQUALS, NOT_EQUALS},
            new OperatorEnum[] {GREATER, SMALLER},
            new OperatorEnum[] {ADDITION, SUBSTRACTION},
            new OperatorEnum[] {DIVISION, MULTIPLICATION, MODULO},
        };
        // @formatter:on
    }

    // setup supportedExpressionTypeMap

    static {
        supportedExpressionTypeMap = new HashMap<>();

        supportedExpressionTypeMap.put(ADDITION, new ArrayList<>());
        supportedExpressionTypeMap.get(ADDITION).add(createBinaryExpressionType(EnumType.INTEGER, "+"));
        supportedExpressionTypeMap.get(ADDITION).add(createBinaryExpressionType(EnumType.FLOAT, "+"));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.STRING, EnumType.STRING));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.INTEGER, EnumType.STRING));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.STRING, EnumType.INTEGER));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.FLOAT, EnumType.STRING));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.STRING, EnumType.FLOAT));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.BOOLEAN, EnumType.STRING));
        supportedExpressionTypeMap.get(ADDITION).add(createStringConcatExpressionType(EnumType.STRING, EnumType.BOOLEAN));

        supportedExpressionTypeMap.put(SUBSTRACTION, new ArrayList<>());
        supportedExpressionTypeMap.get(SUBSTRACTION).add(createBinaryExpressionType(EnumType.INTEGER, "-"));
        supportedExpressionTypeMap.get(SUBSTRACTION).add(createBinaryExpressionType(EnumType.FLOAT, "-"));

        supportedExpressionTypeMap.put(MULTIPLICATION, new ArrayList<>());
        supportedExpressionTypeMap.get(MULTIPLICATION).add(createBinaryExpressionType(EnumType.INTEGER, "*"));
        supportedExpressionTypeMap.get(MULTIPLICATION).add(createBinaryExpressionType(EnumType.FLOAT, "*"));

        supportedExpressionTypeMap.put(DIVISION, new ArrayList<>());
        supportedExpressionTypeMap.get(DIVISION).add(createBinaryExpressionType(EnumType.INTEGER, "/"));
        supportedExpressionTypeMap.get(DIVISION).add(createBinaryExpressionType(EnumType.FLOAT, "/"));

        supportedExpressionTypeMap.put(MODULO, new ArrayList<>());
        supportedExpressionTypeMap.get(MODULO).add(createBinaryExpressionType(EnumType.INTEGER, "%"));
        supportedExpressionTypeMap.get(MODULO).add(createBinaryExpressionType(EnumType.FLOAT, "%"));

        supportedExpressionTypeMap.put(AND, new ArrayList<>());
        supportedExpressionTypeMap.get(AND).add(createBoolBinaryExpressionType("&&"));

        supportedExpressionTypeMap.put(OR, new ArrayList<>());
        supportedExpressionTypeMap.get(OR).add(createBoolBinaryExpressionType("||"));

        supportedExpressionTypeMap.put(OR_EXCLUSIVE, new ArrayList<>());
        supportedExpressionTypeMap.get(OR_EXCLUSIVE).add(createBoolBinaryExpressionType("^"));

        supportedExpressionTypeMap.put(EQUALS, new ArrayList<>());
        supportedExpressionTypeMap.get(EQUALS).add(createCmpExpressionType(EnumType.INTEGER, "=="));
        supportedExpressionTypeMap.get(EQUALS).add(createCmpExpressionType(EnumType.FLOAT, "=="));
        supportedExpressionTypeMap.get(EQUALS).add(createCmpExpressionType(EnumType.STRING, "=="));
        supportedExpressionTypeMap.get(EQUALS).add(createCmpExpressionType(EnumType.BOOLEAN, "=="));
        supportedExpressionTypeMap.get(EQUALS).add(createArrayEqualExpressionType("=="));

        supportedExpressionTypeMap.put(NOT_EQUALS, new ArrayList<>());
        supportedExpressionTypeMap.get(NOT_EQUALS).add(createCmpExpressionType(EnumType.INTEGER, "!="));
        supportedExpressionTypeMap.get(NOT_EQUALS).add(createCmpExpressionType(EnumType.FLOAT, "!="));
        supportedExpressionTypeMap.get(NOT_EQUALS).add(createCmpExpressionType(EnumType.STRING, "!="));
        supportedExpressionTypeMap.get(NOT_EQUALS).add(createCmpExpressionType(EnumType.BOOLEAN, "!="));
        supportedExpressionTypeMap.get(NOT_EQUALS).add(createArrayEqualExpressionType("!="));

        supportedExpressionTypeMap.put(GREATER, new ArrayList<>());
        supportedExpressionTypeMap.get(GREATER).add(createCmpExpressionType(EnumType.INTEGER, ">"));
        supportedExpressionTypeMap.get(GREATER).add(createCmpExpressionType(EnumType.FLOAT, ">"));
        supportedExpressionTypeMap.get(GREATER).add(createCmpExpressionType(EnumType.STRING, ">"));

        supportedExpressionTypeMap.put(SMALLER, new ArrayList<>());
        supportedExpressionTypeMap.get(SMALLER).add(createCmpExpressionType(EnumType.INTEGER, "<"));
        supportedExpressionTypeMap.get(SMALLER).add(createCmpExpressionType(EnumType.FLOAT, "<"));
        supportedExpressionTypeMap.get(SMALLER).add(createCmpExpressionType(EnumType.STRING, "<"));
    }

}
