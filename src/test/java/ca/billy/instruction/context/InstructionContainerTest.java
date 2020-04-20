package ca.billy.instruction.context;

import org.junit.Assert;
import org.junit.Test;

import ca.billy.BillyException;
import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.MethodInstruction;
import ca.billy.type.EnumType;

public class InstructionContainerTest {

    @Test
    public void testNoMain() {
        try {
            new InstructionContainer().build();
        } catch (BillyException e) {
            Assert.assertEquals("no main()", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testFindMethod() {
        InstructionContainer instuctionContainer = new InstructionContainer();
        instuctionContainer.add(new MethodInstruction(instuctionContainer, new MethodDefinition("aMethod")));
        Assert.assertNull(instuctionContainer.findMethod("myMethod"));
        Assert.assertFalse(instuctionContainer.isExistingMethod("myMethod"));

        instuctionContainer.add(new MethodInstruction(instuctionContainer, new MethodDefinition("myMethod")));
        Assert.assertNotNull(instuctionContainer.findMethod("myMethod"));
        Assert.assertTrue(instuctionContainer.isExistingMethod("myMethod"));
    }

    @Test
    public void testFindVariable() {
        InstructionContainer instuctionContainer = new InstructionContainer();
        instuctionContainer.add(new MainAttributeDefinitionInstruction("aAtribute", EnumType.STRING, 0));
        Assert.assertNull(instuctionContainer.findVariable("myAttribute"));
        Assert.assertFalse(instuctionContainer.isExistingVariable("myAttribute"));
        
        instuctionContainer.add(new MainAttributeDefinitionInstruction("myAttribute", EnumType.BOOLEAN, 0));
        Assert.assertNotNull(instuctionContainer.findVariable("myAttribute"));
        Assert.assertTrue(instuctionContainer.isExistingVariable("myAttribute"));
    }

}
