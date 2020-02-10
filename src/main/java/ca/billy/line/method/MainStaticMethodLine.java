package ca.billy.line.method;

import ca.billy.BillyException;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.MethodInstruction;
import ca.billy.line.BillyLineContext;

public class MainStaticMethodLine extends AbstractMethodWithoutParameterLine implements BillyLineContext {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return true;
    }

    @Override
    public MethodInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        String methodName = extractMethodName(line);
        if (instructionContext.isExistingMethod(methodName))
            throw new BillyException("static method already define : " + methodName);
        
        return new MethodInstruction(instructionContext, new MethodDefinition(methodName).setStatic());
    }
}
