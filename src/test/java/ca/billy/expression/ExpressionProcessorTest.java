package ca.billy.expression;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.billy.BillyException;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.ReplaceWrapperExpression;
import ca.billy.expression.instruction.builder.BinaryExpressionBuilder;
import ca.billy.expression.instruction.builder.CmpExpressionBuilder;
import ca.billy.expression.instruction.builder.StringConcatExpressionBuilder;
import ca.billy.expression.instruction.leaf.ConstExpressionInstruction;
import ca.billy.expression.instruction.leaf.MethodExpressionInstruction;
import ca.billy.expression.instruction.leaf.VariableExpressionInstruction;
import ca.billy.expression.instruction.node.NodeExpression;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.type.EnumType;

public class ExpressionProcessorTest {

    @Test
    public void parseSimpleBoolean() {
        IExpressionInstruction res = ExpressionProcessor.parse(" false", EnumType.BOOLEAN, new InstructionContainer());

        Assert.assertTrue(res instanceof ConstExpressionInstruction);
        Assert.assertEquals(new Boolean(false), ((ConstExpressionInstruction) res).getValue());
    }

    @Test
    public void parseSimpleString() {
        IExpressionInstruction res = ExpressionProcessor.parse("\" + \"", EnumType.STRING, new InstructionContainer());

        Assert.assertTrue(res instanceof ConstExpressionInstruction);
        Assert.assertEquals(" + ", ((ConstExpressionInstruction) res).getValue());
    }

    @Test
    public void parseSimpleStringArray() {
        IExpressionInstruction res = ExpressionProcessor.parse(" {\"+\",\"-\"}", EnumType.STRING_ARRAY, new InstructionContainer());

        Assert.assertTrue(res instanceof ConstExpressionInstruction);
        Assert.assertTrue(((ConstExpressionInstruction) res).getValue() instanceof String[]);
    }

    @Test
    public void parseSimpleAdd() {
        IExpressionInstruction res = ExpressionProcessor.parse(" 1 + 3 ", EnumType.INTEGER, new InstructionContainer());

        Assert.assertTrue(res instanceof NodeExpression);
        Assert.assertEquals(new Integer(1), ((ConstExpressionInstruction) ((NodeExpression) res).getLeft()).getValue());
        Assert.assertEquals(new Integer(3), ((ConstExpressionInstruction) ((NodeExpression) res).getRight()).getValue());

        Assert.assertTrue(((NodeExpression) res).getExpressionType().getBuilder() instanceof BinaryExpressionBuilder);
    }

    @Test
    public void parseSimpleEquals() {
        VariableDefinitionInstruction variableDefinitionInstruction = Mockito.mock(VariableDefinitionInstruction.class);
        BillyInstructionContext context = Mockito.mock(BillyInstructionContext.class);

        Mockito.when(context.findVariable("a")).thenReturn(variableDefinitionInstruction);
        Mockito.when(variableDefinitionInstruction.getEnumType()).thenReturn(EnumType.FLOAT);

        IExpressionInstruction res = ExpressionProcessor.parse("33f==a", EnumType.BOOLEAN, context);

        Assert.assertTrue(res instanceof NodeExpression);
        Assert.assertEquals(new Float(33), ((ConstExpressionInstruction) ((NodeExpression) res).getLeft()).getValue());
        Assert.assertTrue(((NodeExpression) res).getRight() instanceof VariableExpressionInstruction);

        Assert.assertTrue(((NodeExpression) res).getExpressionType().getBuilder() instanceof CmpExpressionBuilder);
    }

    @Test
    public void parseSimpleMethod() {
        BillyInstructionContext context = Mockito.mock(BillyInstructionContext.class);
        MethodDefinition methodDefinition = Mockito.mock(MethodDefinition.class);

        Mockito.when(context.findMethod("testMethod")).thenReturn(methodDefinition);
        Mockito.when(methodDefinition.getReturnEnumType()).thenReturn(EnumType.FLOAT_ARRAY);
        Mockito.when(methodDefinition.getArgs()).thenReturn(new EnumType[] { EnumType.INTEGER, EnumType.BOOLEAN });

        IExpressionInstruction res = ExpressionProcessor.parse(" testMethod (1 + 1, false)", EnumType.FLOAT_ARRAY, context);

        Assert.assertTrue(res instanceof ReplaceWrapperExpression);
        Assert.assertTrue(((ReplaceWrapperExpression) res).getExpression() instanceof MethodExpressionInstruction);
    }

    @Test
    public void parseSimpleParenthesesFloat() {
        IExpressionInstruction res = ExpressionProcessor.parse("(12.0)", EnumType.FLOAT, new InstructionContainer());

        Assert.assertTrue(res instanceof ReplaceWrapperExpression);
        Assert.assertTrue(((ReplaceWrapperExpression) res).getExpression() instanceof ConstExpressionInstruction);
        Assert.assertEquals(new Float(12.0), ((ConstExpressionInstruction) ((ReplaceWrapperExpression) res).getExpression()).getValue());
    }

    @Test
    public void parseParenthesesStringOperation() {
        IExpressionInstruction res = ExpressionProcessor.parse("\"test\" == (\"a\" + \"t\") ", EnumType.BOOLEAN, new InstructionContainer());

        Assert.assertTrue(res instanceof NodeExpression);

        Assert.assertEquals("test", ((ConstExpressionInstruction) ((NodeExpression) res).getLeft()).getValue());
        Assert.assertTrue(((NodeExpression) res).getRight() instanceof ReplaceWrapperExpression);

        Assert.assertTrue(((NodeExpression) res).getExpressionType().getBuilder() instanceof CmpExpressionBuilder);

        // Assert the ("a" + "t")
        ReplaceWrapperExpression replace = (ReplaceWrapperExpression) ((NodeExpression) res).getRight();

        Assert.assertTrue(replace.getExpression() instanceof NodeExpression);
        Assert.assertEquals("a", ((ConstExpressionInstruction) ((NodeExpression) replace.getExpression()).getLeft()).getValue());
        Assert.assertEquals("t", ((ConstExpressionInstruction) ((NodeExpression) replace.getExpression()).getRight()).getValue());

        Assert.assertTrue(((NodeExpression) replace.getExpression()).getExpressionType().getBuilder() instanceof StringConcatExpressionBuilder);
    }

    @Test
    public void parseParenthesesInParentheses() {
        IExpressionInstruction res = ExpressionProcessor.parse("(7 - 1) % (123 - (1 * 14))", EnumType.INTEGER, new InstructionContainer());

        Assert.assertTrue(res instanceof NodeExpression);
        Assert.assertTrue(((NodeExpression) res).getLeft() instanceof ReplaceWrapperExpression);
        Assert.assertTrue(((NodeExpression) res).getRight() instanceof ReplaceWrapperExpression);

        Assert.assertTrue(((NodeExpression) res).getExpressionType().getBuilder() instanceof BinaryExpressionBuilder);

        // Assert the (123 - (1 * 14))
        Assert.assertTrue(((ReplaceWrapperExpression) ((NodeExpression) res).getRight()).getExpression() instanceof NodeExpression);
        NodeExpression node = (NodeExpression) ((ReplaceWrapperExpression) ((NodeExpression) res).getRight()).getExpression();

        Assert.assertTrue(node.getLeft() instanceof ConstExpressionInstruction);
        Assert.assertEquals(new Integer(123), ((ConstExpressionInstruction) node.getLeft()).getValue());
        Assert.assertTrue(node.getRight() instanceof ReplaceWrapperExpression);

        Assert.assertTrue(node.getExpressionType().getBuilder() instanceof BinaryExpressionBuilder);

        // Assert the (1 * 14)
        Assert.assertTrue(((ReplaceWrapperExpression) node.getRight()).getExpression() instanceof NodeExpression);
        node = (NodeExpression) ((ReplaceWrapperExpression) node.getRight()).getExpression();

        Assert.assertEquals(new Integer(1), ((ConstExpressionInstruction) node.getLeft()).getValue());
        Assert.assertEquals(new Integer(14), ((ConstExpressionInstruction) node.getRight()).getValue());

        Assert.assertTrue(node.getExpressionType().getBuilder() instanceof BinaryExpressionBuilder);
    }

    @Test
    public void parseMethodInParentheses() {
        BillyInstructionContext context = Mockito.mock(BillyInstructionContext.class);
        MethodDefinition methodDefinition = Mockito.mock(MethodDefinition.class);

        Mockito.when(context.findMethod("a")).thenReturn(methodDefinition);
        Mockito.when(methodDefinition.getReturnEnumType()).thenReturn(EnumType.INTEGER);
        Mockito.when(methodDefinition.getArgs()).thenReturn(new EnumType[0]);

        IExpressionInstruction res = ExpressionProcessor.parse("(a() + 12)", EnumType.INTEGER, context);

        Assert.assertTrue(res instanceof ReplaceWrapperExpression);
        Assert.assertTrue(((ReplaceWrapperExpression) res).getExpression() instanceof NodeExpression);
        Assert.assertTrue(((NodeExpression) ((ReplaceWrapperExpression) res).getExpression()).getLeft() instanceof ReplaceWrapperExpression);

        // Method assert
        ReplaceWrapperExpression replace = (ReplaceWrapperExpression) ((NodeExpression) ((ReplaceWrapperExpression) res).getExpression()).getLeft();
        Assert.assertTrue(replace.getExpression() instanceof MethodExpressionInstruction);
    }

    @Test
    public void parseParenthesesInMethod() {
        BillyInstructionContext context = Mockito.mock(BillyInstructionContext.class);
        MethodDefinition methodDefinition = Mockito.mock(MethodDefinition.class);

        Mockito.when(context.findMethod("pp")).thenReturn(methodDefinition);
        Mockito.when(methodDefinition.getReturnEnumType()).thenReturn(EnumType.BOOLEAN_ARRAY);
        Mockito.when(methodDefinition.getArgs()).thenReturn(new EnumType[] { EnumType.INTEGER });

        IExpressionInstruction res = ExpressionProcessor.parse("pp((1))", EnumType.BOOLEAN_ARRAY, context);

        Assert.assertTrue(res instanceof ReplaceWrapperExpression);
        Assert.assertTrue(((ReplaceWrapperExpression) res).getExpression() instanceof MethodExpressionInstruction);
    }
    
    @Test
    public void parseParenthesesInString() {

        IExpressionInstruction res = ExpressionProcessor.parse("\"(1)\"", EnumType.STRING, new InstructionContainer());

        Assert.assertTrue(res instanceof ConstExpressionInstruction);
        Assert.assertEquals("(1)", ((ConstExpressionInstruction) res).getValue());
    }
    
    @Test
    public void parseInvalidExpected() {
        try {
            ExpressionProcessor.parse("12", EnumType.FLOAT, new InstructionContainer());
        } catch (BillyException b) {
            Assert.assertEquals("The type Float is not assignable for the type Integer", b.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void parseMissingOneParentheses() {
        try {
            ExpressionProcessor.parse("((12)", EnumType.BOOLEAN, new InstructionContainer());
        } catch (BillyException b) {
            Assert.assertEquals("Missing one ')'", b.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void parseUnexpectedParentheses() {
        try {
            ExpressionProcessor.parse("(12))", EnumType.BOOLEAN, new InstructionContainer());
        } catch (BillyException b) {
            Assert.assertEquals("Unexpected ')'", b.getMessage());
            return;
        }
        Assert.fail();
    }
}
