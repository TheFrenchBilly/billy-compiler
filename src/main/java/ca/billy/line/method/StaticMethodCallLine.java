package ca.billy.line.method;

import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.StaticMethodCallInstruction;
import ca.billy.line.BillyLine;
import ca.billy.line.LineWrapper;
import ca.billy.util.MethodUtil;

public class StaticMethodCallLine extends AbstractMethodWithoutParameterLine implements BillyLine {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return true;
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new StaticMethodCallInstruction(MethodUtil.extractMethodName(line.getLine()));
    }

}
