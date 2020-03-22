package ca.billy.instruction.method.call;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import ca.billy.expression.instruction.IExpression;
import ca.billy.instruction.AlwaysValidBillyInstruction;
import ca.billy.instruction.BillyCodeInstruction;

public class PrintInstruction implements BillyCodeInstruction, AlwaysValidBillyInstruction {

    private IExpression expression;

    private boolean endLine;

    public PrintInstruction(IExpression expression) {
        super();
        this.expression = expression;
    }

    public PrintInstruction(IExpression expression, boolean endLine) {
        super();
        this.expression = expression;
        this.endLine = endLine;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        ObjectType printStream = new ObjectType("java.io.PrintStream");

        args.getIl().append(args.getFactory().createFieldAccess("java.lang.System", "out", printStream, Const.GETSTATIC));
        expression.build(args);
        args.getIl().append(
                args.getFactory().createInvoke(
                        "java.io.PrintStream",
                        endLine ? "println" : "print",
                        Type.VOID,
                        new Type[] { expression.getResultType().getBcelType() },
                        Const.INVOKEVIRTUAL));
    }

}
