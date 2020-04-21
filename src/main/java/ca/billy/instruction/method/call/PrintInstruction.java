package ca.billy.instruction.method.call;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import ca.billy.expression.Expression;
import ca.billy.expression.instruction.ConstExpressionInstruction;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.control.loop.ForEachInstruction;
import ca.billy.type.EnumType;

public class PrintInstruction implements BillyCodeInstruction {

    private Expression expression;

    private boolean endLine;

    public PrintInstruction(Expression expression) {
        super();
        this.expression = expression;
    }

    public PrintInstruction(Expression expression, boolean endLine) {
        super();
        this.expression = expression;
        this.endLine = endLine;
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {

        IExpressionInstruction expressionInstruction = expression.compile(args);

        if (expressionInstruction.getResultType().isArray()) {
            printArray(args);
        } else {
            print(args, expressionInstruction, endLine);
        }

    }

    private void print(BillyCodeInstructionArgs args, IExpressionInstruction expression, boolean endLine) {
        args.getIl().append(args.getFactory().createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Const.GETSTATIC));
        expression.build(args);
        args.getIl().append(
                args.getFactory().createInvoke(
                        "java.io.PrintStream",
                        endLine ? "println" : "print",
                        Type.VOID,
                        new Type[] { expression.getResultType().getBcelType() },
                        Const.INVOKEVIRTUAL));
    }

    private void printArray(BillyCodeInstructionArgs args) {
        print(args, new ConstExpressionInstruction(ca.billy.Const.START_SQUARE_BRACKETS + ca.billy.Const.SPACE, EnumType.STRING), false);
        
        ForEachInstruction forEachInstruction = new ForEachInstruction(args.getContext(), "toPrint", expression);
        forEachInstruction.add(new PrintInstruction(new Expression("toPrint + \" \"" , EnumType.STRING)));
        forEachInstruction.build(args);
        
        print(args, new ConstExpressionInstruction(ca.billy.Const.END_SQUARE_BRACKETS, EnumType.STRING), endLine);
    }

}
