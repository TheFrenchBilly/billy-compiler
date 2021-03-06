package ca.billy.instruction.control.loop;

import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.generic.IINC;

import ca.billy.BillyException;
import ca.billy.expression.Expression;
import ca.billy.expression.ExpressionFactory;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.leaf.VariableExpressionInstruction;
import ca.billy.expression.instruction.leaf.array.ArrayAccessExpressionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.TmpContext;
import ca.billy.instruction.method.call.ArrayLengthMethodCallInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class ForEachInstruction extends AbstractLoopInstruction {

    private String indexName;

    private String variableName;

    private Expression arrayExpression;

    public ForEachInstruction(BillyInstructionContext parent, String indexName, String variableName, Expression arrayExpression) {
        super(parent);
        this.indexName = indexName;
        this.variableName = variableName;
        this.arrayExpression = arrayExpression;
    }

    @Override
    protected short getJumpOpCode() {
        return Const.IF_ICMPLT;
    }

    @Override
    protected List<VariableDefinitionInstruction> init(BillyCodeInstructionArgs loopArgs, TmpContext tmpContext) {
        List<VariableDefinitionInstruction> variables = new ArrayList<>();
        IExpressionInstruction array = arrayExpression.compile(loopArgs);

        if (array instanceof VariableExpressionInstruction) {
            variables.add(((VariableExpressionInstruction) array).getVariableDefinitionInstruction());
        } else {
            VariableDefinitionInstruction hiddenArrayVariable = new VariableDefinitionInstruction("hiddenArray", array.getResultType(), arrayExpression);
            hiddenArrayVariable.build(loopArgs);
            tmpContext.add(hiddenArrayVariable);
            add(hiddenArrayVariable);
            variables.add(hiddenArrayVariable);
        }

        VariableDefinitionInstruction hiddenVariable = new VariableDefinitionInstruction(getIndexName(), EnumType.INTEGER, -1);
        hiddenVariable.build(loopArgs);
        tmpContext.add(hiddenVariable);
        add(hiddenVariable);
        variables.add(hiddenVariable);

        return variables;
    }

    @Override
    protected VariableDefinitionInstruction beforeUserInstruction(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs) {
        VariableDefinitionInstruction arrayVariable = retrieveArray(initVariables);
        Expression userVarExp = ExpressionFactory
                .createExpressionWithInstruction(new ArrayAccessExpressionInstruction(arrayVariable, new VariableExpressionInstruction(findIndexVariable())));
        VariableDefinitionInstruction loopVariable = new VariableDefinitionInstruction(variableName, arrayVariable.getEnumType().getArrayType(), userVarExp);
        loopVariable.build(loopArgs);
        add(loopVariable);
        return loopVariable;
    }

    @Override
    protected void increment(BillyCodeInstructionArgs loopArgs) {
        loopArgs.getIl().append(new IINC(findIndexVariable().getIndex(), 1));
    }

    @Override
    protected void expression(List<VariableDefinitionInstruction> initVariables, BillyCodeInstructionArgs loopArgs) {
        findIndexVariable().buildLoad(loopArgs);
        VariableDefinitionInstruction arrayVariable = retrieveArray(initVariables);
        new ArrayLengthMethodCallInstruction(ExpressionFactory.createExpressionWithInstruction(new VariableExpressionInstruction(arrayVariable)), true).build(loopArgs);
    }

    private VariableDefinitionInstruction retrieveArray(List<VariableDefinitionInstruction> initVariables) {
        return initVariables.stream().filter(v -> v.getEnumType().typeMatch(EnumType.ANY_ARRAY)).findFirst().orElseThrow(() -> new BillyException("Should not happen"));
    }

    private VariableDefinitionInstruction findIndexVariable() {
        return findVariable(getIndexName());
    }

    private String getIndexName() {
        return indexName == null ? "hidden" : indexName;
    }
}
