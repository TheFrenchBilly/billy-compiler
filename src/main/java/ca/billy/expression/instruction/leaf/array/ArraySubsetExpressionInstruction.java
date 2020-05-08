package ca.billy.expression.instruction.leaf.array;

import java.util.List;

import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;

import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.OperatorEnum;
import ca.billy.expression.instruction.leaf.LeafExpressionInstruction;
import ca.billy.expression.instruction.leaf.VariableExpressionInstruction;
import ca.billy.expression.instruction.node.NodeExpression;
import ca.billy.expression.type.ExpressionTypeFactory;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.control.loop.WhileInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArraySubsetExpressionInstruction implements LeafExpressionInstruction {

    private VariableDefinitionInstruction arrayVariableDefinitionInstruction;

    // TODO should it be? private IExpression expression;
    // To have expression inside the []
    private LeafExpressionInstruction leftExpression;

    // TODO should it be? private IExpression expression;
    // To have expression inside the []
    private LeafExpressionInstruction rightExpression;

    @Override
    public EnumType getResultType() {
        return arrayVariableDefinitionInstruction.getEnumType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        TmpContext tmpContext = new TmpContext(args.getContext(), arrayVariableDefinitionInstruction.getEnumType());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();

        VariableDefinitionInstruction varDef = new VariableDefinitionInstruction("i", EnumType.INTEGER, ExpressionFactory.createExpressionWithInstruction(leftExpression));

        VariableExpressionInstruction varExpInst = new VariableExpressionInstruction(varDef);

        // Create the loop variable
        varDef.build(args);
        tmpContext.add(varDef);

        // Create a array
        Expression diffExp = ExpressionFactory.createExpressionWithInstruction(
                new NodeExpression(rightExpression, ExpressionTypeFactory.createBinaryExpressionType(EnumType.INTEGER, OperatorEnum.SUBSTRACTION.getOperator()), varExpInst));
        diffExp.build(args);
        args.getIl().append(args.getFactory().createNewArray(arrayVariableDefinitionInstruction.getEnumType().getArrayBcelType(), (short) 1));

        Expression expression = ExpressionFactory.createExpressionWithInstruction(
                new NodeExpression(varExpInst, ExpressionTypeFactory.createCmpExpressionType(EnumType.INTEGER, OperatorEnum.SMALLER.getOperator()), rightExpression));

        new WhileInstruction(tmpContext, expression) {

            @Override
            protected void buildLoopIns(BillyCodeInstructionArgs loopArgs, List<VariableDefinitionInstruction> initVariables, VariableDefinitionInstruction loopVariable) {
                args.getIl().append(InstructionConst.DUP);
                new NodeExpression(varExpInst, ExpressionTypeFactory.createBinaryExpressionType(EnumType.INTEGER, OperatorEnum.SUBSTRACTION.getOperator()), leftExpression)
                        .build(loopArgs);
                new ArrayAccessExpressionInstruction(arrayVariableDefinitionInstruction, varExpInst).build(loopArgs);
                loopArgs.getIl().append(InstructionFactory.createArrayStore(arrayVariableDefinitionInstruction.getEnumType().getArrayBcelType()));
                loopArgs.getIl().append(new IINC(varDef.getIndex(), 1));
            }

        }.build(tmpArgs);
    }
}
