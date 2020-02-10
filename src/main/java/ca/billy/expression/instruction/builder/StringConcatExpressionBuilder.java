package ca.billy.expression.instruction.builder;

import org.apache.bcel.Const;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import ca.billy.bcel.utils.StackUtil;
import ca.billy.expression.instruction.SimpleExpression;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.AllArgsConstructor;

// FIXME create a StrinBuilder for each concat, probably can be better
@AllArgsConstructor
public class StringConcatExpressionBuilder implements ExpressionBuilder {

    private Type leftType;

    @Override
    public void build(SimpleExpression left, SimpleExpression right, BillyCodeInstructionArgs args) {
        createStringBuilder(args);
        left.build(args);
        initStringBuilder(leftType, args);
        appendToStringBuilder(right, args);
        toString(args);
    }

    @Override
    public void build(SimpleExpression right, BillyCodeInstructionArgs args) {
        createStringBuilder(args);
        StackUtil.swap(args.getIl(), 2, right.getType(args.getContext()).getTypeInfo().getBcelType().getSize());
        initStringBuilder(leftType, args);
        appendToStringBuilder(right, args);
        toString(args);
    }

    private void createStringBuilder(BillyCodeInstructionArgs args) {
        args.getIl().append(args.getFactory().createNew(StringBuilder.class.getName()));
        args.getIl().append(InstructionConst.DUP);
    }
    
    private void initStringBuilder(Type type, BillyCodeInstructionArgs args) {
        args.getIl().append(
                args.getFactory().createInvoke(
                        StringBuilder.class.getName(),
                        "<init>",
                        Type.VOID,
                        new Type[] { type },
                        Const.INVOKESPECIAL));
    }

    private void appendToStringBuilder(SimpleExpression exp, BillyCodeInstructionArgs args) {
        exp.build(args);
        args.getIl().append(
                args.getFactory().createInvoke(
                        StringBuilder.class.getName(),
                        "append",
                        new ObjectType(StringBuilder.class.getName()),
                        new Type[] { exp.getResultType().getTypeInfo().getBcelType() },
                        Const.INVOKEVIRTUAL));
    }

    private void toString(BillyCodeInstructionArgs args) {
        args.getIl().append(args.getFactory().createInvoke(StringBuilder.class.getName(), "toString", Type.STRING, Type.NO_ARGS, Const.INVOKEVIRTUAL));
    }

}
