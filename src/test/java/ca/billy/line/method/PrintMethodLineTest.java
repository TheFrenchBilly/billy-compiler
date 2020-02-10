package ca.billy.line.method;

import org.junit.Assert;
import org.junit.Test;

import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.MethodInstruction;
import ca.billy.instruction.method.call.PrintInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class PrintMethodLineTest {

    @Test
    public void testIsValid() {
        PrintMethodLine printMethodLine = new PrintMethodLine();

        Assert.assertFalse(printMethodLine.isValid("", null));
        Assert.assertFalse(printMethodLine.isValid("print()", null));
        Assert.assertTrue(printMethodLine.isValid("print(\"tata\")", null));
    }

    @Test
    public void testCreateBillyInstruction() {
        PrintMethodLine printMethodLine = new PrintMethodLine();

        MethodInstruction variableContainer = new MethodInstruction(new InstructionContainer(), new MethodDefinition("methodName"));
        variableContainer.add(new VariableDefinitionInstruction("patate", EnumType.STRING));
        BillyInstruction result = printMethodLine.createBillyInstruction("print(patate)", variableContainer, new ExpressionProcessor());

        Assert.assertEquals(PrintInstruction.class, result.getClass());
    }
}
