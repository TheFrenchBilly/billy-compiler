package ca.billy.line.method.build.in;

import ca.billy.Const;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.instruction.method.MainInstruction;
import ca.billy.line.BillyLineContext;
import ca.billy.line.LineWrapper;
import ca.billy.line.method.AbstractMethodWithoutParameterLine;

public class MainMethodLine extends AbstractMethodWithoutParameterLine implements BillyLineContext {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return Const.MAIN.equals(methodName);
    }

    @Override
    public MainInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new MainInstruction((InstructionContainer) instructionContext);
    }

}