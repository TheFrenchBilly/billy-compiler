package ca.billy.instruction.method.call;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

public class ReadLineMethodCallInstruction extends StaticMethodCallInstruction {

    public ReadLineMethodCallInstruction(boolean inExpression) {
        super(ca.billy.Const.READ_LINE, inExpression);
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        ObjectType console = new ObjectType("java.io.Console");

        args.getIl().append(args.getFactory().createInvoke("java.lang.System", "console", console, Type.NO_ARGS, Const.INVOKESTATIC));
        args.getIl().append(args.getFactory().createInvoke("java.io.Console", "readLine", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
        if (!inExpression) {
            args.getIl().append(InstructionConst.POP);
        }
    }
}
