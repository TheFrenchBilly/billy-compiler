package ca.billy.instruction.method.call;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.method.MethodDefinition;

public class StaticMethodCallInstruction implements BillyCodeInstruction {

    private String methodName;

    private Expression[] expressions;

    protected boolean inExpression;

    public StaticMethodCallInstruction(String methodName, Expression... expressions) {
        super();
        this.methodName = methodName;
        this.expressions = expressions;
    }

    public StaticMethodCallInstruction(String methodName, boolean inExpression) {
        super();
        this.methodName = methodName;
        this.inExpression = inExpression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        MethodDefinition method;
        if ((method = args.getContext().findMethod(methodName)) == null) {
            throw new BillyException("unable to find methodName : " + methodName);
        }
        if (inExpression && Type.VOID == method.getReturnType()) {
            throw new BillyException(methodName + " return type VOID unexpected");
        }
        if (expressions.length != method.getArgs().length) {
            throw new BillyException(methodName + " expected " + method.getArgs().length + " parameter(s) but got " + expressions.length);
        }

        if (expressions.length == 0) {
            args.getIl().append(args.getFactory().createInvoke("Main", methodName, method.getReturnType(), Type.NO_ARGS, Const.INVOKESTATIC));
        } else {
            // TODO
        }

        if (!Type.VOID.equals(method.getReturnType()) && !inExpression) {
            args.getIl().append(InstructionConst.POP);
        }
    }

}
