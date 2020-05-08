package ca.billy.expression.instruction.leaf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.billy.expression.instruction.leaf.array.ArrayAccessExpressionInstruction;
import ca.billy.expression.instruction.leaf.array.ArraySubsetExpressionInstruction;
import ca.billy.expression.instruction.leaf.array.ReverseArrayAccessExpressionInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class LeafExpressionFactoryTest {

    private BillyInstructionContext context;

    private VariableDefinitionInstruction array;

    @Before
    public void setup() {
        context = Mockito.mock(BillyInstructionContext.class);
        array = Mockito.mock(VariableDefinitionInstruction.class);

        Mockito.when(context.findVariable("variable")).thenReturn(array);

        Mockito.when(array.getEnumType()).thenReturn(EnumType.BOOLEAN_ARRAY);
    }

    @Test
    public void testArrayAcces() {
        LeafExpressionInstruction leaf = LeafExpressionFactory.createLeafExpression("variable  [ 1]", context);

        Assert.assertTrue(leaf instanceof ArrayAccessExpressionInstruction);
    }

    @Test
    public void testReverseArrayAcces() {
        LeafExpressionInstruction leaf = LeafExpressionFactory.createLeafExpression("variable[ :1]", context);

        Assert.assertTrue(leaf instanceof ReverseArrayAccessExpressionInstruction);
    }

    @Test
    public void testArraySubset() {
        LeafExpressionInstruction leaf = LeafExpressionFactory.createLeafExpression("variable[1: 3]", context);

        Assert.assertTrue(leaf instanceof ArraySubsetExpressionInstruction);
    }

}
