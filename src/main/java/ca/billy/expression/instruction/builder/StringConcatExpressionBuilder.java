package ca.billy.expression.instruction.builder;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import ca.billy.bcel.utils.StackUtil;
import ca.billy.expression.instruction.SimpleExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.AllArgsConstructor;

// FIXME create a StringBuilder for each concat, probably can be better
@AllArgsConstructor
public class StringConcatExpressionBuilder implements ExpressionBuilder {

    private Type leftType;

    @Override
    public void build(SimpleExpressionInstruction left, SimpleExpressionInstruction right, BillyCodeInstructionArgs args) {
        createStringBuilder(args);
        left.build(args);
        initStringBuilder(leftType, args);
        appendToStringBuilder(right, args);
        toString(args);
    }

    @Override
    public void build(SimpleExpressionInstruction right, BillyCodeInstructionArgs args) {
        createStringBuilder(args);
        StackUtil.swap(args.getIl(), 2, right.getResultType().getBcelType().getSize());
        initStringBuilder(leftType, args);
        appendToStringBuilder(right, args);
        toString(args);
    }

    private void createStringBuilder(BillyCodeInstructionArgs args) {
        args.getIl().append(args.getFactory().createNew(StringBuilder.class.getName()));
        args.getIl().append(InstructionConst.DUP);
    }

    private void initStringBuilder(Type type, BillyCodeInstructionArgs args) {
        if (type.equals(Type.STRING)) {
            args.getIl().append(args.getFactory().createInvoke(StringBuilder.class.getName(), "<init>", Type.VOID, new Type[] { type }, Const.INVOKESPECIAL));
        } else {
            StackUtil.swap(args.getIl(), 2, type.getSize());
            args.getIl().append(args.getFactory().createInvoke(StringBuilder.class.getName(), "<init>", Type.VOID, new Type[] {}, Const.INVOKESPECIAL));
            args.getIl().append(
                    args
                            .getFactory()
                            .createInvoke(StringBuilder.class.getName(), "append", new ObjectType(StringBuilder.class.getName()), new Type[] { type }, Const.INVOKEVIRTUAL));
        }
    }

    private void appendToStringBuilder(SimpleExpressionInstruction exp, BillyCodeInstructionArgs args) {
        exp.build(args);
        args.getIl().append(
                args.getFactory().createInvoke(
                        StringBuilder.class.getName(),
                        "append",
                        new ObjectType(StringBuilder.class.getName()),
                        new Type[] { exp.getResultType().getBcelType() },
                        Const.INVOKEVIRTUAL));
    }

    private void toString(BillyCodeInstructionArgs args) {
        args.getIl().append(args.getFactory().createInvoke(StringBuilder.class.getName(), "toString", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
    }

}
