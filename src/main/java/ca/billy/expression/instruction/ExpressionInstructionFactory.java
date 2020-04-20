package ca.billy.expression.instruction;

import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import ca.billy.util.MethodUtil;
import ca.billy.util.VariableUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpressionInstructionFactory {

    public static SimpleExpressionInstruction createSimpleExpression(String value, BillyInstructionContext instructionContext) {
        EnumType type = EnumType.findTypeWithValue(value);
        if (type != null) {
            return new ConstExpressionInstruction(type.getValue(value), type);
        } else if (value.indexOf(Const.START_PARENTHESES) != -1 && value.endsWith(Const.END_PARENTHESES)) {
            return createMethodExpression(value, instructionContext);
        } else if (VariableUtil.isValidName(value)) {
            return createVariableExpression(value, instructionContext);
        } else if (VariableUtil.isValidArrayAccess(value)) {
            return createArrayAccesExpression(value, instructionContext);
        }
        throw new BillyException("No available type for the input " + value);
    }

    // FIXME don't support overload for method name
    // FIXME don't support argument
    private static MethodExpressionInstruction createMethodExpression(String value, BillyInstructionContext instructionContext) {

        String methodName = MethodUtil.extractMethodName(value);
        String[] args = MethodUtil.extractParameters(value);
        MethodDefinition methodDefinition = instructionContext.findMethod(methodName);
        if (methodDefinition == null) {
            throw new BillyException("method name not define : " + methodName);
        }
        if (Type.VOID == methodDefinition.getReturnType()) {
            throw new BillyException(methodName + " return type VOID unexpected");
        }
        if (args.length != methodDefinition.getArgs().length) {
            throw new BillyException(methodName + " expected " + methodDefinition.getArgs().length + "parameter(s) but got " + args.length);
        }
        Expression[] argsExp = new Expression[args.length];
        for (int i = 0; i < args.length; ++i) {
            argsExp[i] = new Expression(args[i], methodDefinition.getArgs()[i]);
            // if (!argsExp[i].getResultType().getBcelType().equals(methodDefinition.getArgs()[i].getBcelType())) {
            // throw new BillyException("Unexpected parameter type");
            // }
        }

        return new MethodExpressionInstruction(methodDefinition, argsExp);
    }

    private static SimpleExpressionInstruction createVariableExpression(String value, BillyInstructionContext instructionContext) {
        VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(value);

        if (variableDefinitionInstruction == null) {
            throw new BillyException("variable not define : " + value);
        }

        return new VariableExpressionInstruction(variableDefinitionInstruction);
    }

    private static ArrayAccessExpressionInstruction createArrayAccesExpression(String value, BillyInstructionContext instructionContext) {
        String[] arrayAccess = VariableUtil.splitArrayAccess(value);

        VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(arrayAccess[0]);
        if (variableDefinitionInstruction == null) {
            throw new BillyException("variable not define : " + arrayAccess[0]);
        }
        if (!variableDefinitionInstruction.getEnumType().isArray()) {
            throw new BillyException("the variable is not a array: " + arrayAccess[0]);
        }

        // TODO should expression be inside []?? (not just simpleExpression)
        return new ArrayAccessExpressionInstruction(variableDefinitionInstruction, createSimpleExpression(arrayAccess[1], instructionContext));
    }

}
