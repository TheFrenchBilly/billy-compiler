package ca.billy.expression.instruction;

import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.call.ReadLineMethodCallInstruction;
import ca.billy.type.EnumType;

public class MethodExpression implements SimpleExpression {

    private String methodString;

    private EnumType returnType;

    private EnumType expectedReturn;
    
    public MethodExpression(String methodString) {
        this.methodString = methodString;
    }

    @Override
    public EnumType getResultType() {
        return returnType == null ? EnumType.ANY : returnType;
    }
    
    @Override
    public boolean matchResultType(EnumType type) {
        expectedReturn = type;
        return SimpleExpression.super.matchResultType(type);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        if (expectedReturn != null && !getType(args.getContext()).typeMatch(expectedReturn)) {
            throw new BillyException(
                    "The type " + expectedReturn.getTypeInfo().getName() + " is not assignable for the type " + getType(args.getContext()).getTypeInfo().getName());
        }
        
        // TODO make more that readLine usable
        new ReadLineMethodCallInstruction(true).build(args);
    }
    
    // FIXME don't support overload for method name
    // FIXME don't support argument
    public EnumType getType(BillyInstructionContext context) {
        String methodName = getMethodName();
        MethodDefinition methodDefinition = context.findMethod(methodName);
        if (methodDefinition == null) {
            throw new BillyException("method name not define : " + methodName);
        }
        if (Type.VOID == methodDefinition.getReturnType()) {
            throw new BillyException(methodName + " return type VOID unexpected");
        }
        return methodDefinition.getReturnEnumType();
    }
    
    private String getMethodName() {
        return methodString.substring(0, methodString.indexOf(Const.START_PARENTHESES));
    }
}
