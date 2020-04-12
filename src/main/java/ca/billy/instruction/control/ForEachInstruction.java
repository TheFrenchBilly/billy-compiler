package ca.billy.instruction.control;

import org.apache.bcel.Const;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.POP;

import ca.billy.BillyException;
import ca.billy.bcel.utils.BranchUtils;
import ca.billy.expression.Expression;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.MethodExpressionInstruction;
import ca.billy.expression.instruction.VariableExpressionInstruction;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.call.ArrayLengthMethodCallInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class ForEachInstruction extends VariableInstructionContext implements BillyCodeInstruction {

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
        BranchInstruction endHandle = InstructionFactory.createBranchInstruction(Const.IF_ICMPLT, null);
        GOTO gotoHandle = new GOTO(null);
        
        // Create the variable with the name based on the hidden int variable and the array
        IExpressionInstruction array = arrayExpression.compile(forArgs);
        Expression userVarExp = null;
        if (array instanceof VariableExpressionInstruction) {
            userVarExp = new Expression(((VariableExpressionInstruction) array).getVariableDefinitionInstruction().getName() + "[hidden]", array.getResultType().getArrayType());
        } else {
            // TODO ?
            throw new BillyException("Unsupported expression for the for loop");
        }

        // Create the hidden int variable
        VariableDefinitionInstruction hiddenVariable = new VariableDefinitionInstruction("hidden", EnumType.INTEGER, -1);
        hiddenVariable.build(forArgs);
        getInstructions().add(hiddenVariable);

        // !! TEST FIXME !!
        new VariableDefinitionInstruction(variableName, array.getResultType().getArrayType(), -1).build(forArgs);
        // !! TEST FIXME !!

        BranchUtils.createBranch(gotoHandle, forArgs);
        endHandle.setTarget(args.getIl().append(InstructionConst.NOP));

        VariableDefinitionInstruction userVariable = new VariableDefinitionInstruction(variableName, array.getResultType().getArrayType(), userVarExp);
        userVariable.build(forArgs);
        getInstructions().add(userVariable);

        for (BillyInstruction ins : getInstructions()) {
            if (hiddenVariable != ins && userVariable != ins) {
                ((BillyCodeInstruction) ins).build(forArgs);
            }
        }

        // increment the hidden int variable
        args.getIl().append(new IINC(hiddenVariable.getIndex(), 1));

        gotoHandle.setTarget(args.getIl().append(new NOP()));

        // build the boolean expression
        hiddenVariable.buildLoad(forArgs);
        new ArrayLengthMethodCallInstruction(arrayExpression, true).build(forArgs);
        BranchUtils.createBranch(endHandle, forArgs);
    }

}
