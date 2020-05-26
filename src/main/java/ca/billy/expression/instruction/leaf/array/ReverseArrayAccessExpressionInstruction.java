package ca.billy.expression.instruction.leaf.array;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.Type;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.OperatorEnum;
import ca.billy.expression.instruction.builder.BinaryExpressionBuilder;
import ca.billy.expression.instruction.leaf.ConstExpressionInstruction;
import ca.billy.expression.instruction.leaf.LeafExpressionInstruction;
import ca.billy.expression.instruction.leaf.MethodExpressionInstruction;
import ca.billy.expression.instruction.leaf.VariableExpressionInstruction;
import ca.billy.expression.instruction.node.NodeExpression;
import ca.billy.expression.type.ExpressionTypeFactory;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReverseArrayAccessExpressionInstruction implements LeafExpressionInstruction {

    private VariableDefinitionInstruction arrayVariableDefinitionInstruction;

    // TODO should it be? private IExpression expression;
    // To have expression inside the []
    private LeafExpressionInstruction expression;

    @Override
    public EnumType getResultType() {
        return arrayVariableDefinitionInstruction.getEnumType().getArrayType();
    }

    @Override
    public void build(BillyCodeInstructionArgs args) {
        arrayVariableDefinitionInstruction.buildLoad(args);
        MethodExpressionInstruction methodExp = new MethodExpressionInstruction(
                args.getContext().findMethod(Const.ARRAY_LENGTH_LINE),
                new Expression[] { ExpressionFactory.createExpressionWithInstruction(new VariableExpressionInstruction(arrayVariableDefinitionInstruction)) });

        NodeExpression nodeExpression = new NodeExpression(
                methodExp,
                ExpressionTypeFactory.createBinaryExpressionType(EnumType.INTEGER, OperatorEnum.SUBSTRACTION.getOperator()),
                expression);

        new BinaryExpressionBuilder(OperatorEnum.SUBSTRACTION.getOperator(), Type.INT).build(nodeExpression, new ConstExpressionInstruction(1, EnumType.INTEGER), args);
        args.getIl().append(InstructionFactory.createArrayLoad(arrayVariableDefinitionInstruction.getEnumType().getArrayBcelType()));
    }
}
