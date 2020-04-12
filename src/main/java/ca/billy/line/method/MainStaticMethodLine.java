package ca.billy.line.method;

import ca.billy.BillyException;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.MethodInstruction;
import ca.billy.line.BillyLineContext;
import ca.billy.line.LineWrapper;
import ca.billy.util.MethodUtil;

public class MainStaticMethodLine extends AbstractMethodWithoutParameterLine implements BillyLineContext {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return true;
    }

    @Override
    public MethodInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        String methodName = MethodUtil.extractMethodName(line.getLine());
        if (instructionContext.isExistingLocalMethod(methodName))
            throw new BillyException("static method already define : " + methodName);
        
        return new MethodInstruction(instructionContext, new MethodDefinition(methodName).setStatic());
    }
}
