package ca.billy.line.method;

import ca.billy.Const;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.instruction.method.MainInstruction;
import ca.billy.line.BillyLineContext;

public class MainMethodLine extends AbstractMethodWithoutParameterLine implements BillyLineContext {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return Const.MAIN.equals(methodName);
    }

    @Override
    public MainInstruction createBillyInstruction(String line, BillyInstructionContext instructionContext, ExpressionProcessor expressionProcessor) {
        return new MainInstruction((InstructionContainer) instructionContext);
    }

}