package ca.billy.expression.instruction.builder;

import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.generic.FCMPG;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.bcel.utils.Branch;
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
        Branch gotoBranch = new Branch(new GOTO(null), args, Type.BOOLEAN);
        Branch cmpBranch = new Branch(InstructionFactory.createBranchInstruction(getOperation(args), null), args);
        
        cmpBranch.buildBranch();
        args.getIl().append(new ICONST(1));
        gotoBranch.buildBranch();
        cmpBranch.setTarget(args.getIl().append(new ICONST(0)));
        gotoBranch.setTarget(args.getIl().append(new NOP()));
    }

    private short getOperation(BillyCodeInstructionArgs args) {
        if (type.equals(Type.INT)) {
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
        } else if (type.equals(Type.FLOAT)){
            switch (op) {
                case "==":
                    args.getIl().append(new FCMPL());
                    return Const.IFNE;
                case "!=":
                    args.getIl().append(new FCMPL());
                    return Const.IFEQ;
                case ">":
                    args.getIl().append(new FCMPL());
                    return Const.IFLE;
                case "<":
                    args.getIl().append(new FCMPG());
                    return Const.IFGE;
            }
        }
        throw new BillyException("Should not happen");
    }

}
