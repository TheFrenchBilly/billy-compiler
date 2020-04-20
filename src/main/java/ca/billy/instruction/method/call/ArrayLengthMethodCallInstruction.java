package ca.billy.instruction.method.call;

import org.apache.bcel.generic.InstructionConst;

import ca.billy.expression.Expression;

public class ArrayLengthMethodCallInstruction extends StaticMethodCallInstruction {

    // TODO make it more generic in StaticMethodCallInstruction maybe (parameter)
    private Expression expression;

    public ArrayLengthMethodCallInstruction(Expression expression, boolean inExpression) {
        super(ca.billy.Const.ARRAY_LENGTH_LINE, inExpression);
        this.expression = expression;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        expression.build(args);
        args.getIl().append(InstructionConst.ARRAYLENGTH);

        if (!inExpression) {
            args.getIl().append(InstructionConst.POP);
        }
    }

}
