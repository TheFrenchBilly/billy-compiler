package ca.billy.expression.instruction.leaf;

import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.instruction.leaf.array.ArrayAccessExpressionInstruction;
import ca.billy.expression.instruction.leaf.array.ArraySubsetExpressionInstruction;
import ca.billy.expression.instruction.leaf.array.ReverseArrayAccessExpressionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import ca.billy.util.MethodUtil;
import ca.billy.util.StringUtil;
import ca.billy.util.VariableUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LeafExpressionFactory {

    public static LeafExpressionInstruction createLeafExpression(String value, BillyInstructionContext instructionContext) {
        EnumType type = EnumType.findTypeWithValue(value);
        if (type != null) {
            return new ConstExpressionInstruction(type.getValue(value), type);
        } else if (value.indexOf(Const.START_PARENTHESES) != -1 && value.endsWith(Const.END_PARENTHESES)) {
            return createMethodExpression(value, instructionContext);
        } else if (VariableUtil.isValidName(value)) {
            return createVariableExpression(value, instructionContext);
        } else if (VariableUtil.isValidArrayExpression(value)) {
            return createArrayExpression(value, instructionContext);
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
            argsExp[i] = ExpressionFactory.createExpression(args[i], methodDefinition.getArgs()[i], 0);
        }

        return new MethodExpressionInstruction(methodDefinition, argsExp);
    }

    private static LeafExpressionInstruction createVariableExpression(String value, BillyInstructionContext instructionContext) {
        VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(value);

        if (variableDefinitionInstruction == null) {
            throw new BillyException("variable not define : " + value);
        }

        return new VariableExpressionInstruction(variableDefinitionInstruction);
    }

    // TODO should expression be inside []?? (not just simpleExpression)
    private static LeafExpressionInstruction createArrayExpression(String value, BillyInstructionContext instructionContext) {
        String[] arrayAccess = VariableUtil.splitArrayAccess(value);

        VariableDefinitionInstruction variableDefinitionInstruction = instructionContext.findVariable(arrayAccess[0]);
        if (variableDefinitionInstruction == null) {
            throw new BillyException("variable not define : " + arrayAccess[0]);
        }
        if (!variableDefinitionInstruction.getEnumType().isArray()) {
            throw new BillyException("the variable is not a array: " + arrayAccess[0]);
        }

        if (arrayAccess[1].contains(Const.COLONS)) {
            String[] stringExpressions = StringUtil.splitOnFirst(arrayAccess[1], Const.COLONS);

            if (stringExpressions[0].isEmpty()) {
                return new ReverseArrayAccessExpressionInstruction(variableDefinitionInstruction, createLeafExpression(stringExpressions[1], instructionContext));
            }

            return new ArraySubsetExpressionInstruction(
                    variableDefinitionInstruction,
                    createLeafExpression(stringExpressions[0], instructionContext),
                    createLeafExpression(stringExpressions[1], instructionContext));
        }

        return new ArrayAccessExpressionInstruction(variableDefinitionInstruction, createLeafExpression(arrayAccess[1], instructionContext));
    }

}
