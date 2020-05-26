package ca.billy.expression.instruction.builder;

import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.OperatorEnum;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.leaf.ConstExpressionInstruction;
import ca.billy.expression.instruction.leaf.MethodExpressionInstruction;
import ca.billy.expression.instruction.leaf.VariableExpressionInstruction;
import ca.billy.expression.instruction.node.NodeExpression;
import ca.billy.expression.instruction.node.NodeLaterEvaluateExpression;
import ca.billy.expression.type.ExpressionTypeFactory;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.control.ElseInstruction;
import ca.billy.instruction.control.IfInstruction;
import ca.billy.instruction.control.loop.BreakInstruction;
import ca.billy.instruction.control.loop.ForEachInstruction;
import ca.billy.instruction.variable.VariableAssignementInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArrayEqualsExpressionBuilder implements ExpressionBuilder {

    private String op;

    @Override
    public void build(IExpressionInstruction left, IExpressionInstruction right, BillyCodeInstructionArgs args) {
        // fail fast if not the same type
        if (left.getResultType() != EnumType.ANY_ARRAY && left.getResultType() != EnumType.ANY_ARRAY && left.getResultType() != right.getResultType()) {
            getReturnInstruction(false).build(args);
            return;
        }

        TmpContext tmpContext = new TmpContext(args.getContext());
        BillyCodeInstructionArgs tmpArgs = args.toBuilder().context(tmpContext).build();

        VariableDefinitionInstruction varLeft = getVariableDefinitionIns(tmpArgs, left, "hiddenLeft");
        VariableDefinitionInstruction varRight = getVariableDefinitionIns(tmpArgs, right, "hiddenRight");

        IfInstruction ifInstruction = createIfInstruction(tmpContext, varLeft, varRight);

        ElseInstruction elseInstruction = new ElseInstruction(tmpContext);
        elseInstruction.add(getReturnInstruction(false));

        tmpContext.add(ifInstruction);
        tmpContext.add(elseInstruction);

        ifInstruction.build(tmpArgs);
        elseInstruction.build(tmpArgs);
    }

    private IfInstruction createIfInstruction(TmpContext tmpContext, VariableDefinitionInstruction varLeft, VariableDefinitionInstruction varRight) {
        VariableExpressionInstruction varLeftExp = new VariableExpressionInstruction(varLeft);
        VariableExpressionInstruction varRightExp = new VariableExpressionInstruction(varRight);

        MethodExpressionInstruction leftLength = new MethodExpressionInstruction(
                tmpContext.findMethod(Const.ARRAY_LENGTH_LINE),
                ExpressionFactory.createExpressionWithInstruction(varLeftExp));
        MethodExpressionInstruction rightLength = new MethodExpressionInstruction(
                tmpContext.findMethod(Const.ARRAY_LENGTH_LINE),
                ExpressionFactory.createExpressionWithInstruction(varRightExp));
        IExpressionInstruction lengthExpressionInstruction = new NodeExpression(leftLength, ExpressionTypeFactory.createCmpExpressionType(EnumType.INTEGER, "=="), rightLength);

        VariableDefinitionInstruction varEqualsResult = new VariableDefinitionInstruction(
                "equalsResult",
                EnumType.BOOLEAN,
                ExpressionFactory.createExpressionWithInstruction(getReturnInstruction(true)));

        IfInstruction ifInstruction = new IfInstruction(tmpContext, ExpressionFactory.createExpressionWithInstruction(lengthExpressionInstruction)) {

            @Override
            protected EnumType[] gotoBranchStackType() {
                return new EnumType[] { EnumType.BOOLEAN };
            }
        };

        ForEachInstruction forEachIns = new ForEachInstruction(ifInstruction, "i", "left", ExpressionFactory.createExpressionWithInstruction(varLeftExp));

        Expression leftAcces = ExpressionFactory.createExpression("left", EnumType.ANY);
        Expression rightAcces = ExpressionFactory.createExpression(varRight.getName() + "[i]", EnumType.ANY);
        IExpressionInstruction accesExpressionInstruction = new NodeLaterEvaluateExpression(
                leftAcces,
                ExpressionTypeFactory.createCmpExpressionType(varLeft.getEnumType().getArrayType(), "!="),
                rightAcces);
        IfInstruction ifInForInstruction = new IfInstruction(forEachIns, ExpressionFactory.createExpressionWithInstruction(accesExpressionInstruction));

        ifInForInstruction.add(new VariableAssignementInstruction("equalsResult", ExpressionFactory.createExpressionWithInstruction(getReturnInstruction(false))));
        ifInForInstruction.add(new BreakInstruction());

        forEachIns.add(ifInForInstruction);

        ifInstruction.add(varEqualsResult);
        ifInstruction.add(forEachIns);
        ifInstruction.add(new BillyCodeInstruction() {
            @Override
            public void build(BillyCodeInstructionArgs args) {
                varEqualsResult.buildLoad(args);
            }
        });

        return ifInstruction;
    }

    private VariableDefinitionInstruction getVariableDefinitionIns(BillyCodeInstructionArgs tmpArgs, IExpressionInstruction exp, String name) {
        if (exp instanceof VariableExpressionInstruction) {
            return ((VariableExpressionInstruction) exp).getVariableDefinitionInstruction();
        } else {
            VariableDefinitionInstruction hiddenArrayVariable = new VariableDefinitionInstruction(
                    name,
                    exp.getResultType(),
                    ExpressionFactory.createExpressionWithInstruction(exp));
            hiddenArrayVariable.build(tmpArgs);
            tmpArgs.getContext().add(hiddenArrayVariable);
            return hiddenArrayVariable;
        }
    }

    private ConstExpressionInstruction getReturnInstruction(boolean returnEqualsValue) {
        return new ConstExpressionInstruction(returnEqualsValue ^ OperatorEnum.NOT_EQUALS.getOperator().equals(op), EnumType.BOOLEAN);
    }

}
