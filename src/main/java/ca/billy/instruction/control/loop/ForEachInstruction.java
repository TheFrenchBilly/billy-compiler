package ca.billy.instruction.control.loop;

import org.apache.bcel.Const;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;

import ca.billy.bcel.utils.Branch;
import ca.billy.expression.Expression;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.VariableExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.method.call.ArrayLengthMethodCallInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class ForEachInstruction extends AbstractForInstruction {

    private String variableName;

    private Expression arrayExpression;

    public ForEachInstruction(BillyInstructionContext parent, String variableName, Expression arrayExpression) {
        super(parent);
        this.variableName = variableName;
        this.arrayExpression = arrayExpression;
    }

    // TODO USE DIRECTLY ExpressionInstruction ????
    @Override
    public void build(BillyCodeInstructionArgs args) {
        BillyCodeInstructionArgs forArgs = args.toBuilder().context(this).build();
        TmpContext tmpContext = new TmpContext(args.getContext());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();

        Branch endBranch = new Branch(InstructionFactory.createBranchInstruction(Const.IF_ICMPLT, null), forArgs);
        Branch gotoBranch = new Branch(new GOTO(null), tmpArgs);

        // Create the variable expression with the name based on the hidden int variable and the array
        IExpressionInstruction array = arrayExpression.compile(forArgs);
        Expression userVarExp = null;
        VariableDefinitionInstruction hiddenArrayVariable = null;
        if (array instanceof VariableExpressionInstruction) {
            userVarExp = new Expression(((VariableExpressionInstruction) array).getVariableDefinitionInstruction().getName() + "[hidden]", array.getResultType().getArrayType());
        } else {
            hiddenArrayVariable = new VariableDefinitionInstruction("hiddenArray", array.getResultType(), arrayExpression);
            hiddenArrayVariable.build(forArgs);
            add(hiddenArrayVariable);
            tmpContext.add(hiddenArrayVariable);
            userVarExp = new Expression("hiddenArray[hidden]", array.getResultType().getArrayType());
        }

        // Create the hidden int variable
        VariableDefinitionInstruction hiddenVariable = new VariableDefinitionInstruction("hidden", EnumType.INTEGER, -1);
        hiddenVariable.build(forArgs);
        add(hiddenVariable);
        tmpContext.add(hiddenVariable);
        gotoBranch.buildBranch();

        endBranch.setTarget(args.getIl().append(InstructionConst.NOP));
        VariableDefinitionInstruction userVariable = new VariableDefinitionInstruction(variableName, array.getResultType().getArrayType(), userVarExp);
        userVariable.build(forArgs);
        add(userVariable);

        for (BillyInstruction ins : getInstructions()) {
            if (hiddenVariable != ins && userVariable != ins && hiddenArrayVariable != ins) {
                ((BillyCodeInstruction) ins).build(forArgs);
            }
        }

        // increment the hidden int variable
        args.getIl().append(new IINC(hiddenVariable.getIndex(), 1));
        gotoBranch.setTarget(args.getIl().append(new NOP()));

        // build the boolean expression
        hiddenVariable.buildLoad(forArgs);
        new ArrayLengthMethodCallInstruction(hiddenArrayVariable == null ? arrayExpression : new Expression("hiddenArray", array.getResultType()), true).build(forArgs);
        endBranch.buildBranch();
        
        setBreakTarget(args);
    }

}
