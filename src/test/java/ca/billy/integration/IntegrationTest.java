package ca.billy.integration;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.billy.FileCompiler;
import ca.billy.Log;
import ca.billy.TestHelper;
import ca.billy.line.LineWrapper;

public class IntegrationTest {

    private FileCompiler fileCompiler;

    @Before
    public void setup() {
        Log.enable();
        LineWrapper.reset();
        fileCompiler = new FileCompiler();
    }

    @AfterClass
    public static void after() {
        TestHelper.removeGeneratedFile();
    }

    @Test
    public void testHelloWorld() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/HelloWorld.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("Hello world", res);
    }

    @Test
    public void testHelloWorldWithStaticMethod() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/HelloWorldStatic.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("Hello world", res);
    }

    @Test
    public void testVariableDef() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/VariableDef.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("Hellofalsetrue357", res);
    }

    @Test
    public void testIf() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/If.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("1 2 3 4 5 6 7 8 9 10 done", res);
    }

    @Test
    public void testIfWithFloat() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/IfWithFloat.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("10.0 9.0 8.0 7.0 6.0 5.0 4.0 3.0 2.0 1.0 done", res);
    }

    /** We just compile for this test ! */
    @Test
    public void testTicTacToe() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/TicTacToe.billy");
        fileCompiler.writeByteCode();
    }

}
