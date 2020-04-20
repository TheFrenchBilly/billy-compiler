package ca.billy.instruction.context;

import java.util.List;

import org.apache.bcel.generic.LocalVariableGen;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class VariableInstructionContextTest {

    class VariableInstructionContextTestImpl extends VariableInstructionContext {

        public VariableInstructionContextTestImpl(BillyInstructionContext parent) {
            super(parent);
        }
    }

    private InstructionContainer instructionContainer;
    private VariableInstructionContextTestImpl testImpl;

    @Before
    public void setup() {
        instructionContainer = new InstructionContainer();
        testImpl = new VariableInstructionContextTestImpl(instructionContainer);
    }

    @Test
    public void testGetFrameVariablesOnlyStaticVariable() {
        instructionContainer.add(new MainAttributeDefinitionInstruction("test", EnumType.FLOAT, -1));

        Assert.assertEquals(0, testImpl.getFrameVariables().size());
    }

    @Test
    public void testGetFrameVariablesWithParents() {
        VariableInstructionContextTestImpl level2 = new VariableInstructionContextTestImpl(testImpl);
        VariableInstructionContextTestImpl level3 = new VariableInstructionContextTestImpl(level2);

        addVariable(testImpl, EnumType.INTEGER_ARRAY, 0);
        addVariable(testImpl, EnumType.FLOAT, 1);

        addVariable(level2, EnumType.STRING_ARRAY, 2);
        addVariable(level2, EnumType.BOOLEAN, null);

        addVariable(level3, EnumType.STRING, 3);

        valid(testImpl, EnumType.INTEGER_ARRAY, EnumType.FLOAT);        
        valid(level2, EnumType.INTEGER_ARRAY, EnumType.FLOAT, EnumType.STRING_ARRAY);
        valid(level3, EnumType.INTEGER_ARRAY, EnumType.FLOAT, EnumType.STRING_ARRAY, EnumType.STRING);
    }

    private void valid(VariableInstructionContextTestImpl context, EnumType... expectedTypes) {
        List<EnumType> res = context.getFrameVariables();

        Assert.assertEquals(expectedTypes.length, res.size());
        Assert.assertArrayEquals(expectedTypes, res.stream().toArray());
    }

    private void addVariable(VariableInstructionContextTestImpl context, EnumType type, Integer index) {
        VariableDefinitionInstruction v;
        context.add(v = new VariableDefinitionInstruction("variableName", type, -1));

        // Set the index
        if (index != null) {
            Whitebox.setInternalState(v, "lg", new LocalVariableGen(index, null, null, null, null));
        }
    }

}
