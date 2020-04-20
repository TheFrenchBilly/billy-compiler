package ca.billy.line.method.build.in;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.method.call.PrintInstruction;
import ca.billy.line.LineWrapper;
import ca.billy.line.method.AbstractParentheseLine;
import ca.billy.type.EnumType;
import ca.billy.util.MethodUtil;

public class PrintLineMethodLine extends AbstractParentheseLine {

    @Override
    protected boolean isValidMethodName(String methodName) {
        return Const.PRINT_LINE.equals(methodName);
    }

    @Override
    protected int expectedNbParameter() {
        return 1;
    }

    @Override
    public BillyInstruction createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        return new PrintInstruction(new Expression(MethodUtil.extractParameters(line.getLine())[0], EnumType.ANY, line.getLineNumber()), true);
    }
}
