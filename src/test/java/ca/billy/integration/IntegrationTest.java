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
        assertEquals("Hello world\n", res);
    }

    @Test
    public void testHelloWorldWithStaticMethod() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/HelloWorldStatic.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("Hello world\n", res);
    }

    @Test
    public void testVariableDef() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/VariableDef.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("Hellofalse\ntrue358\n", res);
    }

    @Test
    public void testArray() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/Array.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals(
                "The string array length is 2\n"
                        + "[ Hello world ]\n"
                        + "Hello\n"
                        + "false\n"
                        + "13\n"
                        + "3\n"
                        + "The float array length is 2\n"
                        + "The string array length is 3\n"
                        + "The string array length is 0\n",
                res);
    }

    @Test
    public void testIf() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/If.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("1 2 3 4 5 6 7 8 9 10 done\n", res);
    }

    @Test
    public void testIfWithFloat() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/IfWithFloat.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("10.0 9.0 8.0 7.0 6.0 5.0 4.0 3.0 2.0 1.0 done\n", res);
    }

    @Test
    public void testFor() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/For.billy");
        fileCompiler.writeByteCode();

        String res = TestHelper.run();
        assertEquals("0123456789done\n" + "10987654321done\n" + "adone\n" + "2.04.06.0done\n" + "1-12345 2-12345 3-12345 4-12345 5-12345 \n", res);
    }

    /** We just compile for this test ! */
    @Test
    public void testTicTacToe() throws Exception {
        fileCompiler.compileFile("src/test/resources/integration/TicTacToe.billy");
        fileCompiler.writeByteCode();
    }

}
