package ca.billy.line.method;

import org.junit.Assert;
import org.junit.Test;

import ca.billy.line.LineWrapper;
import ca.billy.line.method.build.in.MainMethodLine;

public class MainMethodLineTest {

    @Test
    public void testIsValid() {
        MainMethodLine mainMethodLine = new MainMethodLine();

        Assert.assertFalse(mainMethodLine.isValid(new LineWrapper(""), null));
        Assert.assertFalse(mainMethodLine.isValid(new LineWrapper("main("), null));
        Assert.assertFalse(mainMethodLine.isValid(new LineWrapper("main)"), null));
        Assert.assertFalse(mainMethodLine.isValid(new LineWrapper("mai()"), null));
        Assert.assertTrue(mainMethodLine.isValid(new LineWrapper("main()"), null));
    }

}
