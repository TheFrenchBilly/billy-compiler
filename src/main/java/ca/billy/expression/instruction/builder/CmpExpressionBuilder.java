package ca.billy.expression.instruction.builder;

import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.bcel.utils.BranchUtils;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CmpExpressionBuilder extends AbstractExpressionBuilder {

    private static final List<String> EQUALS = Arrays.asList("==", "!=");

    private String op;

    private Type type;

    @Override
    protected void build(BillyCodeInstructionArgs args) {
        if (EQUALS.contains(op) && (type instanceof ReferenceType)) {
            createObjectEquals(args);
        } else {
            createCmp(args);
        }
    }

    private void createObjectEquals(BillyCodeInstructionArgs args) {
        args.getIl().append(args.getFactory().createInvoke("java.util.Objects", "equals", Type.BOOLEAN, new Type[] { Type.OBJECT, Type.OBJECT }, Const.INVOKESTATIC));

        // Reverse the boolean response
        if (op.equals("!=")) {
            args.getIl().append(new ICONST(1));
            args.getIl().append(InstructionFactory.createBinaryOperation("^", Type.INT));
        }
    }

    private void createCmp(BillyCodeInstructionArgs args) {
        GOTO gotoInstruction = new GOTO(null);
        BranchInstruction branch = InstructionFactory.createBranchInstruction(getOperation(), null);

        BranchUtils.createBranch(branch, args);

        args.getIl().append(new ICONST(1));
        BranchUtils.createBranch(gotoInstruction, args, Type.BOOLEAN);
        branch.setTarget(args.getIl().append(new ICONST(0)));
        gotoInstruction.setTarget(args.getIl().append(new NOP()));
    }

    private short getOperation() {
        switch (op) {
            case "==":
                return Const.IF_ICMPNE;
            case "!=":
                return Const.IF_ICMPEQ;
            case ">":
                return Const.IF_ICMPLE;
            case "<":
                return Const.IF_ICMPGE;

        }
        throw new BillyException("Should not happen");
    }

}
