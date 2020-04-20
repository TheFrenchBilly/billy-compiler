package ca.billy.erreur;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.billy.BillyException;
import ca.billy.FileCompiler;
import ca.billy.Log;
import ca.billy.util.LineUtil;

public class ErreurIntegrationTest {
    
    private FileCompiler fileCompiler;
    
    @Before
    public void setup() {
        Log.enable();
        LineUtil.resetLineNumber();
        fileCompiler = new FileCompiler();
    }
    
    @Test
    public void testDoubleMain() {     
        try {
            fileCompiler.compileFile("src/test/resources/erreur/DoubleMain.billy");
        } catch (BillyException e) {
            Assert.assertEquals("two main() method - lineNumber=4", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testDoubleMainStaticMethodName() {
        try {
            fileCompiler.compileFile("src/test/resources/erreur/DoubleMainStaticMethodName.billy");
        } catch (BillyException e) {
            Assert.assertEquals("static method already define : world - lineNumber=8", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testInvalidMainStaticMethodCall() {
        try {
            fileCompiler.compileFile("src/test/resources/erreur/InvalidMainStaticMethodCall.billy");
        } catch (BillyException e) {
            Assert.assertEquals("unable to find methodName : world", e.getMessage());
            return;
        }
        Assert.fail();
    }

}
