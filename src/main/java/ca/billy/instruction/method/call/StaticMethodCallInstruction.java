package ca.billy.instruction.method.call;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;

public class StaticMethodCallInstruction implements BillyCodeInstruction {

    private String methodName;
    
    protected boolean inExpression;

    public StaticMethodCallInstruction(String methodName) {
        super();
        this.methodName = methodName;
    }
    
    public StaticMethodCallInstruction(String methodName, boolean inExpression) {
        super();
        this.methodName = methodName;
        this.inExpression = inExpression;
    }

    // TODO args validation
    @Override
    public void valid(BillyInstructionContext instructionContext) {
        if (!instructionContext.isExistingMethod(methodName))
            throw new BillyException("unable to find methodName : " + methodName);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        MethodDefinition method = args.getContext().findMethod(methodName);
        args.getIl().append(args.getFactory().createInvoke("Main", methodName, method.getReturnType(), Type.NO_ARGS, Const.INVOKESTATIC));
        if (!Type.VOID.equals(method.getReturnType()) && !inExpression) {
            args.getIl().append(InstructionConst.POP);
        }
    }

}
